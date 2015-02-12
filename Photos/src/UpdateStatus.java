import twitter4j.Status;  
import twitter4j.StatusUpdate;
import twitter4j.Twitter;  
import twitter4j.TwitterException;  
import twitter4j.TwitterFactory;  
import twitter4j.auth.AccessToken;  
import twitter4j.auth.RequestToken;  
  


import java.io.BufferedReader;  
import java.io.File;
import java.io.IOException;  
import java.io.InputStreamReader;  
  
/** 
 * 更新一条状态（发布一条微博） 
 * Example application that uses OAuth method to acquire access to your account.<br> 
 * This application illustrates how to use OAuth method with Twitter4J.<br> 
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com 
 */  
public final class UpdateStatus {  
    /** 
     * Usage: java twitter4j.examples.tweets.UpdateStatus [text] 
     * 
     * @param args message 
     */  
    public static void main(String[] args) { 
    	UpdateStatus us= new UpdateStatus();
    	
        String text = "Scarlett first post. lalala!!!!!!"; 
        try {  
            Twitter twitter = new TwitterFactory().getInstance();  
            try {  
                // get request token.  
                // this will throw IllegalStateException if access token is already available  
                RequestToken requestToken = twitter.getOAuthRequestToken();  
                System.out.println("Got request token.");  
                System.out.println("Request token: " + requestToken.getToken());  
                System.out.println("Request token secret: " + requestToken.getTokenSecret());  
                AccessToken accessToken = null;  
  
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  
                while (null == accessToken) {  
                    System.out.println("Open the following URL and grant access to your account:");  
                    System.out.println(requestToken.getAuthorizationURL());  
                    System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");  
                    String pin = br.readLine();  
                    try {  
                        if (pin.length() > 0) {  
                            accessToken = twitter.getOAuthAccessToken(requestToken, pin);  
                        } else {  
                            accessToken = twitter.getOAuthAccessToken(requestToken);  
                        }  
                    } catch (TwitterException te) {  
                        if (401 == te.getStatusCode()) {  
                            System.out.println("Unable to get the access token.");  
                        } else {  
                            te.printStackTrace();  
                        }  
                    }  
                }  
                System.out.println("Got access token.");  
                System.out.println("Access token: " + accessToken.getToken());  
                System.out.println("Access token secret: " + accessToken.getTokenSecret());  
            } catch (IllegalStateException ie) {  
                // access token is already available, or consumer key/secret is not set.  
                if (!twitter.getAuthorization().isEnabled()) {  
                    System.out.println("OAuth consumer key/secret is not set.");  
                    System.exit(-1);  
                }  
            }  
             StatusUpdate sta = new StatusUpdate(text);
             sta.setMedia(new File("/Users/Scarlett/Desktop/twi.png"));
            Status status = twitter.updateStatus(sta);  
            System.out.println("Successfully updated the status to [" + status.getText() + "].");  
            System.exit(0);  
        } catch (TwitterException te) {  
            te.printStackTrace();  
            System.out.println("Failed to get timeline: " + te.getMessage());  
            System.exit(-1);  
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
            System.out.println("Failed to read the system input.");  
            System.exit(-1);  
        }
         
    }  
}