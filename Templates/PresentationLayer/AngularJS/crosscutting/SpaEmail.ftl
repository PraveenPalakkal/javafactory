public String sendMail(EmailVo email) throws HexApplicationException {
		System.out.println("in side send email method " + email);
		ResourceBundle bundle = ResourceBundle.getBundle("MailServerCridencials", Locale.ENGLISH);
		EmailValidator emailValidator = EmailValidator.getInstance();
		MailVO mailVO = new MailVO();
		String[] cc=null;
		String[] bc=null;
		try {
		String[] to = email.getTo().split(";");
		if (!emailValidation(to).equals("failed"))
			mailVO.setTos(to);
		else
			return "Failure: please valid To address";

		if (!"".equals(email.getCc())&& null!=email.getCc()){
		 cc = email.getCc().split(";");
		
			if (!emailValidation(cc).equals("failed"))
				mailVO.setTos(cc);
			else
				return "Failure: please valid Cc address";
		}
			if (!"".equals(email.getBc())&& null!=email.getBc()){
		 bc = email.getBc().split(";");

		if (!"".equals(bc[0]))
			if (!emailValidation(bc).equals("failed"))
				mailVO.setTos(bc);
			else
				return "Failure: please valid Bc address";
			}
		if (!emailValidator.isValid(email.getFrom()))
			return "Failure: please valid From address";

		mailVO.setFrom(email.getFrom());
		String host = bundle.getString("SEVER_HOST");
		int port = Integer.parseInt(bundle.getString("SEVER_PORT"));

		mailVO.setSubject(email.getSub());
		if ("".equals(email.getMeg())|| null==email.getMeg())
			return "Failure: Please enter message";
		mailVO.setBody(email.getMeg());
		mailVO.setHost(host);
		mailVO.setPort(port);

		
			new MailingImpl().sendEmail(mailVO);
			return "Success:Email sent successfully";
		} catch(NullPointerException Ne){
			return "Failure: Please enter valid data to fields";
		}catch (MailingException Me) {
			return "Failure: Email sending failed";
		} catch (Exception e) {
			return "Failure: Email sending failed";
		}
	
	
	}

private String emailValidation(String[] email) {
		EmailValidator emailValidator = EmailValidator.getInstance();
		for (int i = 0; i < email.length; i++) {
			if (!emailValidator.isValid(email[i]))
				return "failed";
		}
		return "success";
	}