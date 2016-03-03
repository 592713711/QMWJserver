package message.response;

import message.MessageTag;
import message.request.Request;
import model.MyLocation;

/**
 * 响应对方位置
 * Created by zsg on 2016/3/3.
 */
public class LocationRes extends Request {
    public MyLocation myLocation;

    public LocationRes() {
        tag = MessageTag.LOCATION_RES;
    }
}
