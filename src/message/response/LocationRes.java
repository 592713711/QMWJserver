package message.response;

import message.MessageTag;
import message.request.Request;
import model.MyLocation;

/**
 * ��Ӧ�Է�λ��
 * Created by zsg on 2016/3/3.
 */
public class LocationRes extends Request {
    public MyLocation myLocation;

    public LocationRes() {
        tag = MessageTag.LOCATION_RES;
    }
}
