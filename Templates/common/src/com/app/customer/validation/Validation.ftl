package com.app.customer.validation;

import com.app.customer.vo.${customerVo};
import com.hjsf.validator.EmailAddreeValidator;
import com.hjsf.validator.HexValidationException;
import com.hjsf.validator.NumericValidator;
import com.hjsf.validator.PhoneNumberValidator;
import com.hjsf.validator.ValidationRules;
import com.hjsf.validator.Validator;

public class Validation {
	public void  isCustomerValidate(${customerVo} customer) throws HexValidationException{
		String message="";
	NumericValidator numberValidator=null;
	EmailAddreeValidator emailAddreeValidator=null;
	PhoneNumberValidator phoneNumberValidator=null;
	ValidationRules validationRule=null;
	Validator validator=null;
		try{
		validationRule=ValidationRules.getInstance();
		 validator=Validator.getInstance();
		numberValidator=NumericValidator.getInstance();
		emailAddreeValidator=EmailAddreeValidator.getInstance();
		phoneNumberValidator=PhoneNumberValidator.getInstance();

		numberValidator.numberValidation(${idConvert});
		validationRule.setFieldValue(customer.getName());
		validationRule.setNotEmptey(true);
		validationRule.setIsNotNull(true);
		message=" Customer Name";
		validator.isValidDate(validationRule);
		message="";
		emailAddreeValidator.emailValidation(customer.getEmail());
		message="";
		phoneNumberValidator.isValidatePhoneNumber(customer.getPhone());
		validationRule.setFieldValue(customer.getAddress());
		validationRule.setNotEmptey(true);
		validationRule.setIsNotNull(true);
		message=" Customer Address";
		validator.isValidDate(validationRule);
		message="";
		validationRule.setFieldValue(customer.getOrders());
		validationRule.setNotEmptey(true);
		validationRule.setIsNotNull(true);
		message=" Customer Order";
		validator.isValidDate(validationRule);
		
		validationRule.setFieldValue(customer.getAction());
		message="";
		validationRule.setNotEmptey(true);
		validationRule.setIsNotNull(true);
		message=" Customer Action";
		validator.isValidDate(validationRule);
		}catch (HexValidationException e) {
			throw new HexValidationException(e.getMessage()+message);
		}catch (NullPointerException ne) {
			throw new HexValidationException("Fields are mandatory");
		}
		
	}
}
