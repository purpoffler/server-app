package layer.writters;

import layer.dto.UserPackage;
import config.ServerConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class PlainWriter extends Writer {
    private final ServerConfig serverConfig = ServerConfig.getInstance();
    private final String plainFileName = serverConfig.getPlainFileName();

    public void doWrite(UserPackage userPackage) throws IOException {
        File file = getFile(plainFileName);
        try (FileWriter writer = new FileWriter(file, true)) {
            log.debug("Записываем в file: " + userPackage.date() + " " + userPackage.ip() + " " + userPackage.data());
            writer.write(userPackage.date() + " " + userPackage.ip() + " " + userPackage.data());
            writer.append('\n');
            writer.flush();
        }
    }
}
