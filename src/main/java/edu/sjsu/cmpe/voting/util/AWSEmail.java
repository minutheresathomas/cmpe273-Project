package edu.sjsu.cmpe.voting.util;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

public class AWSEmail {
	public void sendMail(final String fromEmail, 
			final String toEmail, 
			final String subject, 
			final String emailBody) {

		SendEmailRequest request = new SendEmailRequest()
		.withSource(fromEmail);

		List<String> toAddresses = new ArrayList<String>();
		toAddresses.add(toEmail);
		Destination dest = new Destination().withToAddresses(toAddresses);

		// set fromEmail as BCC
		List<String> toBccAddresses = new ArrayList<String>();
		toBccAddresses.add(fromEmail);
		dest.setBccAddresses(toBccAddresses);
		request.setDestination(dest);

		Content subjContent = new Content().withData(subject);
		Message msg = new Message().withSubject(subjContent);

		// Include a body in HTML formats.
		Content htmlContent = new Content().withData(emailBody);
		Body body = new Body().withHtml(htmlContent);
		msg.setBody(body);

		request.setMessage(msg);

		// Set AWS access credentials.
		AmazonSimpleEmailServiceClient client =
				new AmazonSimpleEmailServiceClient(
						new BasicAWSCredentials("AKIAIG3MNOEPPAEMJNPQ", "ipVDJEkPYu8JsKF8B7uajvBHEgmZab2rHdYVWUUW"));

		// Call Amazon SES to send the message. 
		try {
			client.sendEmail(request);

		} catch (AmazonClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
