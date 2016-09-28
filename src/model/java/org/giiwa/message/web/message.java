package org.giiwa.message.web;

import java.util.ArrayList;
import java.util.List;

import org.giiwa.core.bean.Beans;
import org.giiwa.core.bean.Helper.W;
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
    this.show("/message/popup.html");
  }

  @Path(path = "inbox", login = true)
  public void inbox() {
    int s = this.getInt("s");
    int n = this.getInt("n", 20, "number.per.page");

    Beans<Message> bs = Message.load(W.create("to", login.getId()).sort("created", -1), s, n);
    this.set(bs, s, n);

    this.show("/message/inbox.html");
  }

  @Path(path = "outbox", login = true)
  public void outbox() {
    int s = this.getInt("s");
    int n = this.getInt("n", 20, "number.per.page");

    Beans<Message> bs = Message.load(W.create("_from", login.getId()).sort("created", -1), s, n);
    this.set(bs, s, n);

    this.show("/message/outbox.html");
  }

}
