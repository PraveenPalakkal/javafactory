public String AuthenticationCheck(UserInformation Information) throws HexApplicationException {
		LdapAuthentication authentication = new LdapAuthentication();
		System.out.println("in side AuthenticationCheck method " + Information);

		try {

			if (authentication.checkAuthentication(Information.getName(), Information.getPassword())) {
				System.out.println("Authentication success for user");
				return "Success:Authenticated";

			} else {
				return "Failure:not Authorized user";
			}

		} catch (Exception e) {
			return "Failure: not Authorized user";
		}
		
	}