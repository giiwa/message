package org.giiwa.message.web;

import java.util.ArrayList;
import java.util.List;

import org.giiwa.framework.web.Model;
import org.giiwa.framework.web.Path;
import org.giiwa.message.bean.Message;

public class message extends Model {

  @Path(path = "popup", login = true)
  public void popup() {
    long total = Message.count(login.getId());
    long unread = Message.unread(login.getId());
    List<String> list = new ArrayList<String>();
    if (unread > 0) {
      list.add(lang.get("message.you_have_unread", unread));
    }
    this.set("list", list);
    ;
    this.show("/message/popup.html");
  }
}
