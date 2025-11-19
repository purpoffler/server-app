package layer.writters;

import layer.dto.UserPackage;
import layer.logger.CustomLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Writer {
    protected final CustomLogger log = new CustomLogger(getClass().getSimpleName());

    public void write(UserPackage userPackage) {
        try {
            doWrite(userPackage);
            log.debug("Запись прошла успешно");
        } catch (IOException e) {
            log.error("Ошибка записи", e);
        }
    }

    public abstract void doWrite(UserPackage userPackage) throws IOException;

    public File getFile(String fileName) {
        Path path = Paths.get(fileName);
        return new File(path.toUri());
    }
}
