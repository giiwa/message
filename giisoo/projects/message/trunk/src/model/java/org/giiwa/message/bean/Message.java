package org.giiwa.message.bean;

import org.giiwa.core.bean.Bean;
import org.giiwa.core.bean.Beans;
import org.giiwa.core.bean.Table;
import org.giiwa.core.bean.Helper;
import org.giiwa.core.bean.Helper.V;
import org.giiwa.core.bean.Helper.W;
import org.giiwa.core.bean.Column;
import org.giiwa.core.bean.UID;
import org.giiwa.core.bean.X;
import org.giiwa.framework.bean.User;

/**
 * Demo bean
 * 
 * @author joe
 * 
 */
@Table(name = "gi_message")
public class Message extends Bean {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final int   FLAG_NEW         = 1;
  public static final int   FLAG_READ        = 2;
  public static final int   FLAG_REPLY       = 3;
  public static final int   FLAG_FORWARD     = 4;

  @Column(name = "to")
  long                      to;

  @Column(name = "_from")
  long                      from;

  @Column(name = "flag")
  int                       flag;

  @Column(name = "category")
  String                    category;

  @Column(name = "title")
  String                    title;

  @Column(name = "content")
  String                    content;

  @Column(name = "deleted_to")
  int                       deleted_to;

  @Column(name = "deleted_from")
  int                       deleted_from;

  public int getDeleted_to() {
    return deleted_to;
  }

  public int getDeleted_from() {
    return deleted_from;
  }

  public long getTo() {
    return to;
  }

  private User to_obj;

  public User getTo_obj() {
    if (to_obj == null) {
      to_obj = User.loadById(to);
    }
    return to_obj;
  }

  public long getFrom() {
    return from;
  }

  private User from_obj;

  public User getFrom_obj() {
    if (from_obj == null) {
      from_obj = User.loadById(from);
    }
    return from_obj;
  }

  public String getCategory() {
    return category;
  }

  public int getFlag() {
    return flag;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  // ------------

  public static long send(long to, long from, String content) {
    return create(V.create("to", to).set("_from", from).set("content", content).set("flag", FLAG_NEW));
  }

  public static int mark(long id, int flag) {
    return update(id, V.create("flag", flag));
  }

  public static long create(V v) {
    /**
     * generate a unique id in distribute system
     */
    long id = UID.next("message.id");
    try {
      while (exists(id)) {
        id = UID.next("message.id");
      }
      Helper.insert(v.set(X.ID, id), Message.class);
      return id;
    } catch (Exception e1) {
      log.error(e1.getMessage(), e1);
    }
    return -1;
  }

  public static boolean exists(long id) {
    try {
      return Helper.exists(id, Message.class);
    } catch (Exception e1) {
      log.error(e1.getMessage(), e1);
    }
    return false;
  }

  public static int update(long id, V v) {
    return Helper.update(id, v, Message.class);
  }

  public static Beans<Message> load(W q, int s, int n) {
    return Helper.load(q, s, n, Message.class);
  }

  public static Message load(long id) {
    return Helper.load(id, Message.class);
  }

  public static long count(long to) {
    return Helper.count(W.create("to", to), Message.class);
  }

  public static long unread(long to) {
    return Helper.count(W.create("to", to).and("flag", FLAG_NEW), Message.class);
  }
}
