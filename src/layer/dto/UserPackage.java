package layer.dto;

import layer.enums.ExpectedDataType;

public record UserPackage(String signature, String dataLength, ExpectedDataType dataType, String data,
                          String clientCRC32, String ip, String date) {
}
