

Google
	https://developers.google.com/identity/protocols/OAuth2InstalledApp
	Probably should just go the Google API way...
	
	Start with same model as before... then maybe enhance to copy and paste... if you can't do the 'webserver redirect thing'
	
	Split out the authentication because that's super useful!
	
	
	Embedding a browser
		JXBrowser
			http://blog.teamdev.com/2015/12/jxbrowser-for-maven-projects.html
			http://jxbrowser.support.teamdev.com/support/solutions/articles/9000012865-quick-start-guide-for-javafx-developers
			
		http://stackoverflow.com/questions/8374365/integrating-javafx-2-0-webview-into-a-swing-java-se-6-application/8413289#8413289
		
		http://docs.oracle.com/javafx/2/webview/jfxpub-webview.htm
		http://stackoverflow.com/questions/48249/is-there-a-way-to-embed-a-browser-in-java
		
		
		
## Expanding to Inbox
	It would be nice to indicate how many things are in the Inbox... with a break down to unread, snoozed (but back) and reminders
	_Currently no Inbox API_
	
## Icon Scaling
	http://stackoverflow.com/questions/4216123/how-to-scale-a-bufferedimage
	
	
	
## TODO:
	* Polish up Gmail side of things
	
		* Add tests
		* Use Java FX browser for authorization
			This would be slick... but would it be shady?  Too easy to intercept the key strokes and steal passwords?
		* Give more information in the popup menu?
		* Proper logging!
		* Put it on GitHub (- the secret?)
		* Polish up the process:
			Application Icon?
			Process Name?
			