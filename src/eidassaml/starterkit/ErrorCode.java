/* 
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * 
 * Date: 09 Feb 2016
 * Authors: Governikus GmbH & Co. KG
 * 
*/
package eidassaml.starterkit;

import java.util.ResourceBundle;

import org.opensaml.saml2.core.StatusCode;


/**
 * Codes to identify various error situations for the eID-Server. Clients (both those communicating with the
 * respective module and the receiver of the SAML response itself) should be able to display a comprehensive
 * message for each of these codes or handle the respective situation automatically. Please note that the SAML
 * status codes are insufficient to cover all specific situations in sufficient detail.
 * 
 * @author TT
 * 
 */
public enum ErrorCode
{
  /**
   * Conversation finished successfully.
   */
  SUCCESS(StatusCode.SUCCESS_URI),
  /**
   * Error situation only specified by details. Do not use if there is a more appropriate value!
   */
  ERROR(StatusCode.RESPONDER_URI),
  /**
   * Unspecified error, see log file. May be returned to browser outside a SAML-response!
   */
  INTERNAL_ERROR(StatusCode.RESPONDER_URI),
  /**
   * Request specifies AssertionConsumerURL but is not signed - will be rejected. May be returned to browser
   * outside a SAML-response!
   */
  UNSIGNED_ASSERTIONCONSUMER_URL(StatusCode.REQUESTER_URI),
  /**
   * No pending SAML request for the specified requestID. May be returned to browser outside a SAML-response!
   */
  INVALID_SESSION_ID(StatusCode.REQUESTER_URI),
  /**
   * There are too many open sessions, this session will not be created.
   */
  TOO_MANY_OPEN_SESSIONS(StatusCode.REQUESTER_URI),
  /**
   * SAML requestID not specified in the subsequent request. May be returned to browser outside a
   * SAML-response!
   */
  MISSING_REQUEST_ID(StatusCode.REQUESTER_URI),
  /**
   * SAML request was not signed correctly
   */
  SIGNATURE_CHECK_FAILED(StatusCode.REQUESTER_URI),
  /**
   * The SAML message was not signed but should be
   */
  SIGNATURE_MISSING(StatusCode.REQUESTER_URI),
  /**
   * There is a syntax error in the request so it cannot be parsed. May be returned to browser outside a
   * SAML-response!
   */
  ILLEGAL_REQUEST_SYNTAX(StatusCode.REQUESTER_URI),
  /**
   * Authorization failed
   */
  AUTHORIZATION_FAILED(StatusCode.AUTHN_FAILED_URI),
  /**
   * Cannot get SAML response because another client action is needed first. May be returned to browser
   * outside a SAML-response!
   */
  AUTHORIZATION_UNFINISHED(StatusCode.REQUESTER_URI),
  /**
   * The SAML request does not specify one of the providers in the eID-Servers configuration. May be returned
   * to browser outside a SAML-response!
   */
  UNKNOWN_PROVIDER(StatusCode.REQUEST_UNSUPPORTED_URI),
  /**
   * The Autent Server is not in HOT state - will not process any requests. May be returned to browser outside
   * a SAML-response!
   */
  SYSTEM_NOT_HOT(StatusCode.RESPONDER_URI),
  /**
   * The configuration of the eID-Servers seems invalid. See log file of the application server.
   */
  ILLEGAL_CONFIGURATION(StatusCode.RESPONDER_URI),
  /**
   * The required client side resource cannot be accessed due to user does not give permission / PIN or so.
   */
  CANNOT_ACCESS_CREDENTIALS(StatusCode.RESPONDER_URI),
  /**
   * A certificate used for authorization is not valid.
   */
  INVALID_CERTIFICATE(StatusCode.AUTHN_FAILED_URI),
  /**
   * Cannot access this part of server in this way. May be returned to browser outside a SAML-response!
   */
  ILLEGAL_ACCESS_METHOD(StatusCode.REQUEST_UNSUPPORTED_URI),
  /**
   * SOAP response from client has wrong format.
   */
  SOAP_RESPONSE_WRONG_SYNTAX(StatusCode.AUTHN_FAILED_URI),
  /**
   * accessing without https is not allowed.
   */
  UNENCRYPTED_ACCESS_NOT_ALLOWED(StatusCode.REQUEST_UNSUPPORTED_URI),
  /**
   * Time restriction of SAML assertion not met
   */
  OUTDATED_ASSERTION(StatusCode.REQUESTER_URI),
  /**
   * The request was initially created for another destination.
   */
  WRONG_DESTINATION(StatusCode.REQUESTER_URI),

  /**
   * some asynchronous step finished for a session which did not expect it.
   */
  UNEXPECTED_EVENT(StatusCode.REQUESTER_URI),
  /**
   * Time restriction of SAML request not met
   */
  OUTDATED_REQUEST(StatusCode.REQUESTER_URI),
  /**
   * This request is from fare in the future.
   */
  REQUEST_FROM_FUTURE(StatusCode.REQUESTER_URI),
  /**
   * The request has an ID which is not unique among the IDs of all received requests. May be returned to
   * browser outside a SAML-response!
   */
  DUPLICATE_REQUEST_ID(StatusCode.REQUESTER_URI),
  /**
   * Some error was reported from the eID-Server.
   */
  EID_ERROR(StatusCode.RESPONDER_URI),
  /**
   * Some error was reported from the eCardAPI
   */
  ECARD_ERROR(StatusCode.AUTHN_FAILED_URI),
  /**
   * The client requested to read some attributes from the nPA which are not allowed by the CVC.
   */
  EID_MISSING_TERMINAL_RIGHTS(StatusCode.RESPONDER_URI),
  /**
   * The requests misses the argument for {0}.
   */
  EID_MISSING_ARGUMENT(StatusCode.RESPONDER_URI),
  /**
   * the password has expired and is therefore invalid
   */
  PASSWORD_EXPIRED(StatusCode.AUTHN_FAILED_URI),
  /**
   * the password has been locked because of too many failed tries
   */
  PASSWORD_LOCKED(StatusCode.AUTHN_FAILED_URI),
  /**
   * Encrypted input data cannot be decrypted - contained information will be missing
   */
  CANNOT_DECRYPT(StatusCode.REQUESTER_URI),
  /**
   * The psk is not fell formed or not long enough.
   */
  ILLEGAL_PSK(StatusCode.REQUESTER_URI),
  /**
   * The authentication client reported an error
   */
  CLIENT_ERROR(StatusCode.AUTHN_FAILED_URI),
  /**
   * The proxy count raced 0
   */
  PROXY_COUNT_EXCEEDED(StatusCode.PROXY_COUNT_EXCEEDED_URI),
  /**
   * Non of the IdPs given in the IDPList in the SAML Request are supported.
   */
  NO_SUPPORTED_IDP(StatusCode.NO_SUPPORTED_IDP_URI), ;

  /**
   * prefix to make all the minor error codes look URN-like
   */
  public static final String URN_PREFIX = "urn:bos-bremen.de:SAML:minorCode:";

  private ErrorCode(String samlStatus)
  {
    this.samlStatus = samlStatus;
  }

  private final String samlStatus;

  private static ResourceBundle messages = ResourceBundle.getBundle("eidassaml.starterkit.errorcodes");

  /**
   * Return human-readable text describing this code.
   * 
   * @param details additional information to use in the text.
   */
  public String toDescription(String... details)
  {
    String result = messages.getString(super.toString());
    if (details != null)
    {
      for ( int i = 0 ; i < details.length ; i++ )
      {
        result = result.replace("{" + i + "}", details[i] == null ? "" : details[i]);
      }
    }
    return result;
  }

  /**
   * Return the SAML status to be returned if this error occurred. Note that several different errors may map
   * to the same SAML status code.
   */
  public String getSamlStatus()
  {
    return samlStatus;
  }
  
  public static ErrorCode GetValueOf(String s)
  {
	  if(SUCCESS.getSamlStatus().equals(s))
	  {
		  return SUCCESS;
	  }
	  
	  if(ERROR.getSamlStatus().equals(s))
	  {
		  return ERROR;
	  }
	  
	  if(INTERNAL_ERROR.getSamlStatus().equals(s))
	  {
		  return INTERNAL_ERROR;
	  }
	  
	  if(UNSIGNED_ASSERTIONCONSUMER_URL.getSamlStatus().equals(s))
	  {
		  return UNSIGNED_ASSERTIONCONSUMER_URL;
	  }
	  
	  if(INVALID_SESSION_ID.getSamlStatus().equals(s))
	  {
		  return INVALID_SESSION_ID;
	  }
	  
	  if(TOO_MANY_OPEN_SESSIONS.getSamlStatus().equals(s))
	  {
		  return TOO_MANY_OPEN_SESSIONS;
	  }
	  
	  if(MISSING_REQUEST_ID.getSamlStatus().equals(s))
	  {
		  return MISSING_REQUEST_ID;
	  }
	  
	  if(SIGNATURE_CHECK_FAILED.getSamlStatus().equals(s))
	  {
		  return SIGNATURE_CHECK_FAILED;
	  }
	  
	  if(SIGNATURE_MISSING.getSamlStatus().equals(s))
	  {
		  return SIGNATURE_MISSING;
	  }
	  
	  if(ILLEGAL_REQUEST_SYNTAX.getSamlStatus().equals(s))
	  {
		  return ILLEGAL_REQUEST_SYNTAX;
	  }
	  
	  if(AUTHORIZATION_FAILED.getSamlStatus().equals(s))
	  {
		  return AUTHORIZATION_FAILED;
	  }
	  
	  if(AUTHORIZATION_UNFINISHED.getSamlStatus().equals(s))
	  {
		  return AUTHORIZATION_UNFINISHED;
	  }
	  
	  if(UNKNOWN_PROVIDER.getSamlStatus().equals(s))
	  {
		  return UNKNOWN_PROVIDER;
	  }
	  
	  if(SYSTEM_NOT_HOT.getSamlStatus().equals(s))
	  {
		  return SYSTEM_NOT_HOT;
	  }
	  
	  if(ILLEGAL_CONFIGURATION.getSamlStatus().equals(s))
	  {
		  return ILLEGAL_CONFIGURATION;
	  }
	  
	  if(CANNOT_ACCESS_CREDENTIALS.getSamlStatus().equals(s))
	  {
		  return CANNOT_ACCESS_CREDENTIALS;
	  }
	  
	  if(INVALID_CERTIFICATE.getSamlStatus().equals(s))
	  {
		  return INVALID_CERTIFICATE;
	  }
	  
	  if(ILLEGAL_ACCESS_METHOD.getSamlStatus().equals(s))
	  {
		  return ILLEGAL_ACCESS_METHOD;
	  }
	  
	  if(SOAP_RESPONSE_WRONG_SYNTAX.getSamlStatus().equals(s))
	  {
		  return SOAP_RESPONSE_WRONG_SYNTAX;
	  }
	  
	  if(UNENCRYPTED_ACCESS_NOT_ALLOWED.getSamlStatus().equals(s))
	  {
		  return UNENCRYPTED_ACCESS_NOT_ALLOWED;
	  }
	  
	  if(OUTDATED_ASSERTION.getSamlStatus().equals(s))
	  {
		  return OUTDATED_ASSERTION;
	  }
	  
	  if(WRONG_DESTINATION.getSamlStatus().equals(s))
	  {
		  return WRONG_DESTINATION;
	  }
	  
	  if(UNEXPECTED_EVENT.getSamlStatus().equals(s))
	  {
		  return UNEXPECTED_EVENT;
	  }
	  
	  if(OUTDATED_REQUEST.getSamlStatus().equals(s))
	  {
		  return OUTDATED_REQUEST;
	  }
	  
	  if(REQUEST_FROM_FUTURE.getSamlStatus().equals(s))
	  {
		  return REQUEST_FROM_FUTURE;
	  }
	  
	  if(DUPLICATE_REQUEST_ID.getSamlStatus().equals(s))
	  {
		  return DUPLICATE_REQUEST_ID;
	  }
	  
	  if(EID_ERROR.getSamlStatus().equals(s))
	  {
		  return EID_ERROR;
	  }
	  
	  if(ECARD_ERROR.getSamlStatus().equals(s))
	  {
		  return ECARD_ERROR;
	  }
	  
	  if(EID_MISSING_TERMINAL_RIGHTS.getSamlStatus().equals(s))
	  {
		  return EID_MISSING_TERMINAL_RIGHTS;
	  }
	  
	  if(EID_MISSING_ARGUMENT.getSamlStatus().equals(s))
	  {
		  return EID_MISSING_ARGUMENT;
	  }
	  
	  if(CANNOT_DECRYPT.getSamlStatus().equals(s))
	  {
		  return CANNOT_DECRYPT;
	  }
	  
	  if(CLIENT_ERROR.getSamlStatus().equals(s))
	  {
		  return CLIENT_ERROR;
	  }
	  	  
	  return null;
  }
}
