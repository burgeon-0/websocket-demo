package org.bg181.jw.server;

import com.google.gson.Gson;
import lombok.Data;

/**
 * @author Sam Lu
 * @date 2022/3/23
 */
@Data
public class Message {

    private String from;

    private String to;

    private String content;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
