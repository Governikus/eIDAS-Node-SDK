Getting started
======

Check out the project and run the default target of the build.xml.
A dist folder will be created where you will find the JAR File.

## Usage 

```JAVA
/*
First let us init the lib and load some keystores (Utils provides a method to read just a .cer file too ):
*/
public static void setUp() throws ConfigurationException {
             EidasSaml.Init(); //init opensaml too
             try {
                    keyPair = Utils.ReadPKCS12(Main.class.getResourceAsStream("saml-sign.p12"), "111111".toCharArray());
                    keyPair2 = Utils.ReadPKCS12(
                                  Main.class.getResourceAsStream("saml-encr.p12"),
                                  "1111111".toCharArray()); //PIN
             } catch (UnrecoverableKeyException e) {
                    e.printStackTrace();
             } catch (KeyStoreException e) {
                    e.printStackTrace();
             } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
             } catch (CertificateException e) {
                    e.printStackTrace();
             } catch (IOException e) {
                    e.printStackTrace();
             }      
       }

/*
Now let us create a MetaXML:
This sample shows how to create a meta.xml for a eidas middleware service. if you want to create a xml for a eidas-connector
use EidasSaml.CreateMetaDataNode(...);
If you have to parse a metadata.xml use EidasSaml.ParseMetaDataNode() or EidasSaml.ParseMetaDataService()
*/
       private static void CreateMetaData() throws CertificateEncodingException, IOException, XMLParserException, UnmarshallingException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException{
             EidasOrganisation organization = new EidasOrganisation("Gov", "Gov AG", "http://localhost", "de-de");
             EidasContactPerson person = new EidasContactPerson("Gov AG", "Hans", "Meyer", "street 1, 10000 city", "+4901000015","test@test.de");
             List<EidasNameIdType> supportedNameIdTypes = new ArrayList<EidasNameIdType>();
             supportedNameIdTypes.add(EidasNameIdType.Transient);
             supportedNameIdTypes.add(EidasNameIdType.Unspecified);
             EidasMetadataService meta = new EidasMetadataService("_" + Utils.GenerateUniqueID(), //id
                           "Service", //entityid
                           new Date(), //validUntil
                           keyPair.getCert(), //signature Cert 
                           keyPair2.getCert(), //encryption Cert
                           organization, person,"https://localhost/HTTPPostReceiverServlet","https://localhost/ReceiverServlet",supportedNameIdTypes);
             ArrayList<EidasNaturalPersonAttributes> list = new ArrayList<>();
             list.add(EidasNaturalPersonAttributes.FamilyName);
             list.add(EidasNaturalPersonAttributes.FirstName);
             EidasSigner _signer = new EidasSigner(true, keyPair.getKey(), keyPair.getCert(), "SHA256");
             byte[] res = meta.generate(list, _signer);
             String xml = new String(res,Constants.UTF8_CHARSET);
       }

/*
Now create a SAML Request:
*/
private static void CreateRequest() throws CertificateEncodingException, ConfigurationException, IOException, XMLParserException, UnmarshallingException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException{
             String destination = "http://eu-middleware/receiver";
             String issuer = "http://testSP";
             EidasSigner _signer = new EidasSigner(true, keyPair.getKey(),
                           keyPair.getCert());
             Map<EidasNaturalPersonAttributes, Boolean> _requestedAttributes = new HashMap<EidasNaturalPersonAttributes, Boolean>();
             _requestedAttributes.put(EidasNaturalPersonAttributes.FirstName, true); //true if required
             _requestedAttributes
                           .put(EidasNaturalPersonAttributes.FamilyName, false);
             _requestedAttributes.put(EidasNaturalPersonAttributes.PersonIdentifier,true);

             byte[] result = EidasSaml.CreateRequest(issuer, destination, _signer,
                           _requestedAttributes, EidasRequestSectorType.Public,
                           EidasNameIdType.Transient, EidasLoA.High);
             //if u like to see the request as XML string
             String requestSAML = new String(result, Constants.UTF8_CHARSET);
       }
/*
Now parse the response from the eu-middleware:
*/
private static void ParseResponse(byte[] incomingSAMLResponse) throws ConfigurationException, XMLParserException, UnmarshallingException, IOException{
             
try(ByteArrayInputStream is = new ByteArrayInputStream(incomingSAMLResponse)){
                    
                    try {
                    
                        java.security.cert.X509Certificate cert = (java.security.cert.X509Certificate)Utils.readCert(NewReceiverServlet.class.getResourceAsStream("saml-sign.pem.crt"),
    	                "x509");
                    
                           EidasResponse eidasResponse = EidasSaml.ParseResponse(is, 
                                        new Utils.X509KeyPair[]{keyPair}, //decrypter
                                        new X509Certificate[]{cert} //signature authors
                           );
                           
                           //get and progress the requested values
                           for(EidasAttribute attribute : eidasResponse.getAttributes())
                           {
                                  if(attribute.getNaturalPersonAttributeType() == EidasNaturalPersonAttributes.FamilyName)
                                  {
                                        FamilyNameAttribute familyNameAttribute = (FamilyNameAttribute)attribute;
                                        String name = familyNameAttribute.getValue();
                                  }
                           }
                           
                    } catch (ErrorCodeException e) {
                           e.printStackTrace();
                    }
             }
       }

```
