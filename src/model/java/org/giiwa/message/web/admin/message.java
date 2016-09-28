package org.giiwa.message.web.admin;

import org.giiwa.core.bean.Beans;
import org.giiwa.core.bean.X;
import org.giiwa.core.json.JSON;
import org.giiwa.core.bean.Helper.V;
import org.giiwa.core.bean.Helper.W;
import org.giiwa.message.bean.Message;
import org.giiwa.framework.web.Model;
import org.giiwa.framework.web.Path;

public class message extends Model {

  @Path(login = true, access = "access.config.admin")
  public void onGet() {
    int s = this.getInt("s");
    int n = this.getInt("n", 20, "number.per.page");

    W q = W.create();
    String name = this.getString("name");

    if (!X.isEmpty(name) && path == null) {
      q.and("name", name, W.OP_LIKE);
      this.set("name", name);
    }
    Beans<Message> bs = Message.load(q, s, n);
    this.set(bs, s, n);

    this.show("/admin/message.index.html");
  }

  @Path(path = "detail", login = true, access = "access.config.admin")
  public void detail() {
    long id = this.getLong("id");
    Message d = Message.load(id);
    this.set("b", d);
    this.set("id", id);
    this.show("/admin/message.detail.html");
  }

  @Path(path = "delete", login = true, access = "access.config.admin")
  public void delete() {
    long id = this.getLong("id");
    Message.delete(id);
    JSON jo = new JSON();
    jo.put(X.STATE, 200);
    this.response(jo);
  }

  @Path(path = "create", login = true, access = "access.config.admin")
  public void create() {
    if (method.isPost()) {
      V v = V.create();
      v.set("_from", login.getId());
      v.set("to", this.getLong("to"));
      v.set("content", this.getHtml("content"));
      v.set("flag", Message.FLAG_NEW);
      long id = Message.create(v);

      this.set(X.MESSAGE, lang.get("create.success"));
      onGet();
      return;
    }

    this.show("/admin/message.create.html");
  }

}