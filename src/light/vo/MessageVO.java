package light.vo;

import com.google.gson.Gson;

public class MessageVO {
 
    private String message;
    private String type;
    private String to;
     
    // 사용자가 보낸 메세지 정보를 담는 역활을 한다.
    public static MessageVO convertMessage( String source ) {
        MessageVO message = new MessageVO();
        Gson gson = new Gson();
        // 사용자가 보낸 json 타입의 정보를 자바 객체에 넣어준다.
        message = gson.fromJson(source, MessageVO.class);
         
        return message;
    }
     
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
}