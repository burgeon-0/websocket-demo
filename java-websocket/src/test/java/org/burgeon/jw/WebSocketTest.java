package org.burgeon.jw;

import cn.hutool.core.util.HexUtil;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * @author Sam Lu
 * @date 2022/3/23
 */
public class WebSocketTest {

    private static final String WS_ACCEPT = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

    @Test
    public void getSecWebSocketAccept() throws NoSuchAlgorithmException {
        String SecWebSocketKey = "8JqnYcvwuNmlYf9tO2dayQ==";
        String SecWebSocketAccept = sha1(SecWebSocketKey.getBytes(), WS_ACCEPT.getBytes());
        System.out.println(SecWebSocketAccept);

        SecWebSocketAccept = sha1((SecWebSocketKey + WS_ACCEPT).getBytes());
        System.out.println(SecWebSocketAccept);
    }

    @Test
    public void maskData() {
        byte[] bytes = HexUtil.decodeHex("363155");
        byte[] mask = HexUtil.decodeHex("0433551e");

        // mask data
        byte[] result = maskData(bytes, mask);
        System.out.println(HexUtil.encodeHexStr(result));

        // unmask data
        byte[] result2 = maskData(result, mask);
        System.out.println(HexUtil.encodeHexStr(result2));
    }

    @Test
    public void zipData() throws DataFormatException {
        byte[] bytes = HexUtil.decodeHex("320200");

        byte[] unzips = unzip(bytes);
        System.out.println(HexUtil.encodeHexStr(unzips));
    }

    private String sha1(byte[]... input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        for (byte[] bytes : input) {
            md.update(bytes);
        }
        byte[] result = md.digest();
        return Base64.getEncoder().encodeToString(result);
    }

    private byte[] maskData(byte[] bytes, byte[] mask) {
        int maskIndex = 0;
        byte[] result = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = (byte) (bytes[i] ^ mask[maskIndex++] & 0xFF);
            if (maskIndex == 4) {
                maskIndex = 0;
            }
        }
        return result;
    }

    private byte[] unzip(byte[] input) throws DataFormatException {
        // Decompress the bytes
        byte[] output = new byte[100];
        Inflater decompresser = new Inflater(true);
        decompresser.setInput(input, 0, input.length);
        int len = decompresser.inflate(output);
        decompresser.end();

        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = output[i];
        }
        return result;
    }

}
