package layer.writters;

import layer.dto.UserPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Writter {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public void write(UserPackage userPackage) {
        try {
            doWrite(userPackage);
            log.debug("Запись прошла успешно [{}]", getClass().getSimpleName());
        } catch (IOException e) {
            log.error("Ошибка записи [{}]", getClass().getSimpleName(), e);
        }
    }

    public abstract void doWrite(UserPackage userPackage) throws IOException;

    public File getFile(String fileName) {
        Path path = Paths.get(".", fileName).toAbsolutePath();
        return new File(path.toUri());
    }
}
