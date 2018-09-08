package storage;

import exceptions.StorageException;
import model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    protected Path directory;

    protected abstract void write(Resume resume, OutputStream os) throws IOException;
    protected abstract Resume read(InputStream is) throws IOException;

    public AbstractPathStorage(String dir) {
        Objects.requireNonNull(dir, "not null");
        this.directory = Paths.get(dir);
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(directory.getFileName() + " is not directory");
        }
        if (!Files.isReadable(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(directory.getFileName() + " is npt readable/writable");
        }
    }

    @Override
    protected List<Resume> getAll() {
        List<Resume> list = new ArrayList<>();
        try {
            Files.list(directory).forEach(path -> list.add(getResume(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void saveResume(Path path, Resume resume) {
        try {
            Files.createFile(path);
            updateResume(path, resume);
        } catch (IOException e) {
            throw new StorageException("IO Exeption", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void updateResume(Path path, Resume resume) {
        try {
            write(resume,new BufferedOutputStream(new FileOutputStream(path.toString())));
        } catch (IOException e) {
            throw new StorageException("IO Exeption", path.getFileName().toString(), e);
        }
    }

    @Override
    protected Path getKey(String uuid) {
        return Paths.get(directory.toString(), uuid);
    }

    @Override
    protected void deleteResume(Path path) {
        try {
            if(Files.deleteIfExists(path)) {
            }
        } catch (IOException e) {
            throw new StorageException("File not found ",path.toString(), e);
        }
    }

    @Override
    protected Resume getResume(Path path) {
        try {
            return read(new BufferedInputStream(new FileInputStream(path.toString())));
        } catch (IOException e) {
            throw new StorageException("Resume not found", path.getFileName().toString(), e);
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::deleteResume);
            System.out.println("Direcrory: '" + directory + "' is empty");
        } catch (IOException e) {
            throw new StorageException("Read Error", null, e);
        }
    }

    @Override
    public int size() {
        try {
            return (int)Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("Error read", null, e);
        }
    }
}