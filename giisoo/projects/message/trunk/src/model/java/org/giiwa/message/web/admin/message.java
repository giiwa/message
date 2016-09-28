package org.giiwa.message.web.admin;

import org.giiwa.core.bean.Beans;
import org.giiwa.core.bean.X;
import org.giiwa.core.json.JSON;
import org.giiwa.core.bean.Helper.V;
import org.giiwa.core.bean.Helper.W;
import org.giiwa.message.bean.Message;
import org.giiwa.framework.bean.User;
import org.giiwa.framework.web.Model;
import org.giiwa.framework.web.Path;

public class message extends Model {

  @Path(login = true, access = "access.config.admin")
  public void onGet() {
    int s = this.getInt("s");
    int n = this.getInt("n", 20, "number.per.page");

    W q = W.create();

    long to = this.getLong("to");
    if (to > 0 && X.isEmpty(path)) {
      q.and("to", to);
      this.set("to", to);
    }

    long from = this.getLong("from");
    if (from > 0 && X.isEmpty(path)) {
      q.and("_from", to);
      this.set("from", to);
    }

    Beans<Message> bs = Message.load(q, s, n);
    this.set(bs, s, n);

    Beans<User> bs1 = User.load(W.create(), 0, 1000);
    this.set("users", bs1 == null ? null : bs1.getList());

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

  @Path(path = "create", login = true, access = "access.config.admin")
  public void create() {
    if (method.isPost()) {
      V v = V.create();
      long from = this.getLong("from");
      v.set("_from", from);
      v.set("to", this.getLong("to"));
      v.set("category", this.getString("category"));
      v.set("title", this.getString("title"));
      v.set("content", this.getHtml("content"));
      v.set("flag", Message.FLAG_NEW);

      long id = Message.create(v);

      this.set(X.MESSAGE, lang.get("create.success"));
      onGet();
      return;
    }

    Beans<User> bs = User.load(W.create(), 0, 1000);
    this.set("users", bs == null ? null : bs.getList());

    this.show("/admin/message.create.html");
  }

}
