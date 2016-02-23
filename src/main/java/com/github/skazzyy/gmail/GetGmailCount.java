package com.github.skazzyy.gmail;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;

public class GetGmailCount {
    /** Application name. */
    private static final String APPLICATION_NAME = "Get Gmail Count";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/gmail-java-quickstart.json");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials at ~/.credentials/gmail-java-quickstart.json
     */
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_LABELS);

    static {
        // workaround for issue https://github.com/google/google-http-java-client/issues/315
        final java.util.logging.Logger buggyLogger = java.util.logging.Logger.getLogger(FileDataStoreFactory.class.getName());
        buggyLogger.setLevel(java.util.logging.Level.SEVERE);

        try {
            GetGmailCount.HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            GetGmailCount.DATA_STORE_FACTORY = new FileDataStoreFactory(GetGmailCount.DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = GetGmailCount.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GetGmailCount.JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GetGmailCount.HTTP_TRANSPORT, GetGmailCount.JSON_FACTORY, clientSecrets, GetGmailCount.SCOPES).setDataStoreFactory(GetGmailCount.DATA_STORE_FACTORY)
                .setAccessType("offline").build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to " + GetGmailCount.DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Gmail client service.
     *
     * @return an authorized Gmail client service
     * @throws IOException
     */
    public static Gmail getGmailService() throws IOException {
        Credential credential = GetGmailCount.authorize();
        return new Gmail.Builder(GetGmailCount.HTTP_TRANSPORT, GetGmailCount.JSON_FACTORY, credential).setApplicationName(GetGmailCount.APPLICATION_NAME).build();
    }

    public final static String INBOX_LABEL_ID = "INBOX";
    public final static String DEFAULT_USER_ID = "me";

    protected static Gmail service = null;
    protected final static Object mutex = new Object();

    /**
     * Initialize a static {@link Gmail} service in a thread safe manner
     *
     * @throws IOException
     */
    protected static void init() throws IOException {
        if(GetGmailCount.service != null) {
            return;
        }
        synchronized (GetGmailCount.mutex) {
            if(GetGmailCount.service == null) {
                GetGmailCount.service = GetGmailCount.getGmailService();
            }
        }
    }

    public int getUnreadCount() throws IOException {
        GetGmailCount.init();
        Label inbox = GetGmailCount.service.users().labels().get(GetGmailCount.DEFAULT_USER_ID, GetGmailCount.INBOX_LABEL_ID).execute();
        return inbox.getMessagesUnread();
    }


    public static void main(String[] args) throws IOException {
        GetGmailCount.init();

        Label inbox = GetGmailCount.service.users().labels().get(GetGmailCount.DEFAULT_USER_ID, GetGmailCount.INBOX_LABEL_ID).execute();
        System.out.println("inbox.getName() = " + inbox.getName());
        System.out.println("inbox.getId() = " + inbox.getId());
        System.out.println("inbox.getType() = " + inbox.getType());
        System.out.println("inbox.getMessagesTotal() = " + inbox.getMessagesTotal());
        System.out.println("inbox.getMessagesUnread() = " + inbox.getMessagesUnread());
        System.out.println("inbox.getThreadsTotal() = " + inbox.getThreadsTotal());
        System.out.println("inbox.getThreadsUnread() = " + inbox.getThreadsUnread());

    }

}