package org.giiwa.message.web;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.giiwa.core.bean.X;
import org.giiwa.core.task.Task;
import org.giiwa.framework.web.IListener;
import org.giiwa.framework.web.Module;
import org.giiwa.message.bean.Message;

public class MessageListener implements IListener {

  static Log log = LogFactory.getLog(MessageListener.class);

  @Override
  public void onStart(Configuration conf, Module m) {
    // TODO Auto-generated method stub
    log.info("message is starting ...");

    new Task() {

      @Override
      public void onExecute() {
        Message.cleanup();
      }

    }.schedule(X.AMINUTE);
  }

  @Override
  public void onStop() {
    // TODO Auto-generated method stub

  }

  @Override
  public void uninstall(Configuration conf, Module m) {
    // TODO Auto-generated method stub

  }

  @Override
  public void upgrade(Configuration conf, Module m) {
    // TODO Auto-generated method stub

  }

}
