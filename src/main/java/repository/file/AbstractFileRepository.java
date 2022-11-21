package repository.file;

import domain.Entity;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    private final String fileName;

    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadFromFile();
    }

    /**
     * Loads from file all data into memory
     */
    private void loadFromFile() {
        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(
                    line -> {
                        E entity = extractEntity(Arrays.asList(line.split(";")));
                        super.save(entity);
                    }
            );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public E save(E entity) {
        if (super.save(entity) != null) {
            writeToFile(entity);
            return null;
        }
        return entity;
    }

    /**
     * Writes to file data corresponding to a given entity
     * @param entity the entity whose data is going to be written to file
     */
    private void writeToFile(E entity) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true))) {
            bufferedWriter.write(createEntityAsString(entity));
            bufferedWriter.newLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reloads all data into file from memory
     */
    private void updateFile() {
        try {
            new FileWriter(fileName, false).close();
            super.findAll().forEach(this::writeToFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public E delete(ID id) {
        E entity = super.delete(id);
        if (entity != null) {
            updateFile();
        }
        return entity;
    }

    @Override
    public E update(E entity) {
        if (super.update(entity) != null) {
            updateFile();
            return null;
        }
        return entity;
    }

    /**
     * Creates an entity from given attributes
     * @param attr list of String containing the attributes for the entity
     * @return the entity created from the attributes
     */
    protected abstract E extractEntity(List<String> attr);

    /**
     * Creates a String with entity's attributes. The String represents the
     * format in which the entity will be saved to file.
     * @param entity the entity to be converted to String
     * @return String to be saved to file containing the attributes of entity
     */
    protected abstract String createEntityAsString(E entity);
}
