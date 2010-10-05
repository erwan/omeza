package controllers;

import play.i18n.Messages;
import play.mvc.Controller;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

public class Jabber extends Controller {

    public static void receive(String from, String to, String body, String stanza) {
        help(from);
        renderText("");
    }

    private static void help(String jid) {
        String message = Messages.get("xmpp.help");
        send(jid, message);
    }

    private static void send(String jid, String body) {
        Message msg = new MessageBuilder()
            .withRecipientJids(new JID(jid))
            .withBody(body)
            .build();
        XMPPServiceFactory.getXMPPService().sendMessage(msg);
    }

}
