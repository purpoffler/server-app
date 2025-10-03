package layer.writters;

import layer.dto.UserPackage;
import utlis.ServerConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class PlainWritter extends Writter {
    private final String plainFileName = ServerConfig.getPlainFileName();

    public void doWrite(UserPackage userPackage) throws IOException {
        File file = getFile(plainFileName);
        try (FileWriter writer = new FileWriter(file, true)) {
            log.debug("Записываем в file: " + userPackage.getDate() + " " + userPackage.getIp() + " " + userPackage.getData());
            writer.write(userPackage.getDate() + " " + userPackage.getIp() + " " + userPackage.getData());
            writer.append('\n');
            writer.flush();
        }
    }
}
