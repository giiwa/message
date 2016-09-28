package org.giiwa.message.web;

import java.util.ArrayList;
import java.util.List;

import org.giiwa.core.bean.Beans;
import org.giiwa.core.bean.Helper.V;
import org.giiwa.core.bean.Helper.W;
import org.giiwa.core.json.JSON;
import org.giiwa.core.bean.X;
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

    W q = W.create("to", login.getId()).and("deleted_to", 1, W.OP_NEQ).sort("created", -1);
    String name = this.getString("name");
    if (!X.isEmpty(name) && X.isSame("inbox", this.path)) {
      q.and("content", name, W.OP_LIKE);
      this.set("name", name);
    }
    Beans<Message> bs = Message.load(q, s, n);
    this.set(bs, s, n);

    this.show("/message/inbox.html");
  }

  @Path(path = "outbox/delete", login = true)
  public void outbox_delete() {
    JSON jo = JSON.create();
    long id = this.getLong("id");
    Message m = Message.load(id);
    if (m != null && m.getFrom() == login.getId()) {
      Message.update(id, V.create("deleted_from", 1).set("updated", V.ignore));
      jo.put(X.STATE, 200);
      jo.put(X.MESSAGE, "ok");
    } else {
      jo.put(X.STATE, 201);
      jo.put(X.MESSAGE, "failed");
    }
    this.response(jo);
  }

  @Path(path = "inbox/delete", login = true)
  public void inbox_delete() {
    JSON jo = JSON.create();
    long id = this.getLong("id");
    Message m = Message.load(id);
    if (m != null && m.getTo() == login.getId()) {
      Message.update(id, V.create("deleted_to", 1).set("updated", V.ignore));
      jo.put(X.STATE, 200);
      jo.put(X.MESSAGE, "ok");
    } else {
      jo.put(X.STATE, 201);
      jo.put(X.MESSAGE, "failed");
    }
    this.response(jo);
  }

  @Path(path = "outbox", login = true)
  public void outbox() {
    int s = this.getInt("s");
    int n = this.getInt("n", 20, "number.per.page");

    W q = W.create("_from", login.getId()).and("deleted_from", 1, W.OP_NEQ).sort("created", -1);
    String name = this.getString("name");
    if (!X.isEmpty(name) && X.isSame("inbox", this.path)) {
      q.and("content", name, W.OP_LIKE);
      this.set("name", name);
    }

    Beans<Message> bs = Message.load(q, s, n);
    this.set(bs, s, n);

    this.show("/message/outbox.html");
  }

  @Path(path = "send", login = true)
  public void send() {
    if(method.isPost()) {
      
    }
    this.show("/message/send.html");
  }

}
