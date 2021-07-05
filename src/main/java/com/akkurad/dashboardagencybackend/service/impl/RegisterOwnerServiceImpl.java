package com.akkurad.dashboardagencybackend.service.impl;

import com.akkurad.dashboardagencybackend.dao.IOwnerDao;
import com.akkurad.dashboardagencybackend.model.Owner;
import com.akkurad.dashboardagencybackend.service.IRegisterOwnerService;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

@Service
@Slf4j
public class RegisterOwnerServiceImpl implements IRegisterOwnerService {

    @Autowired
    private IOwnerDao iOwnerDao;


    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    public static HashMap<String, Object> init() throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvalidArgumentException, CryptoException {
        HashMap<String, Object> settings = new HashMap<>();
        // Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put("pemFile", "/home/aho/go/src/github.com/ahohm/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem");
//        props.put("pemFile", "/my-app/ca.org1.example.com-cert.pem");
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance("https://localhost:7054", props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newCouchDBWallet(new URL("http://localhost:5984"),"wallet");

        X509Identity adminIdentity = (X509Identity)wallet.get("admin");
        if (adminIdentity == null) {
            System.out.println("\"admin\" needs to be enrolled and added to the wallet first");
            return null;
        }
        User admin = new User() {

            @Override
            public String getName() {
                return "admin";
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return "org1.department1";
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {

                    @Override
                    public PrivateKey getKey() {
                        return adminIdentity.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return Identities.toPemString(adminIdentity.getCertificate());
                    }
                };
            }

            @Override
            public String getMspId() {
                return "Org1MSP";
            }

        };

        settings.put("adminIdentity",adminIdentity);
        settings.put("admin",admin);
        settings.put("caClient", caClient);
        settings.put("wallet", wallet);
        settings.put("cryptoSuite", cryptoSuite);
        settings.put("props", props);

        return settings;

    }

    public static void run() throws Exception {

        // Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put("pemFile", "/home/aho/go/src/github.com/ahohm/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem");
//        props.put("pemFile", "/my-app/ca.org1.example.com-cert.pem");
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance("https://localhost:7054", props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newCouchDBWallet(new URL("http://localhost:5984"),"wallet");

        // Check to see if we've already enrolled the user.
        if (wallet.get("1appUser") != null) {
            System.out.println("An identity for the user \"1appUser\" already exists in the wallet");
            return;
        }

        X509Identity adminIdentity = (X509Identity)wallet.get("admin");
        if (adminIdentity == null) {
            System.out.println("\"admin\" needs to be enrolled and added to the wallet first");
            return;
        }
        User admin = new User() {

            @Override
            public String getName() {
                return "admin";
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return "org1.department1";
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {

                    @Override
                    public PrivateKey getKey() {
                        return adminIdentity.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return Identities.toPemString(adminIdentity.getCertificate());
                    }
                };
            }

            @Override
            public String getMspId() {
                return "Org1MSP";
            }

        };

        // Register the user, enroll the user, and import the new identity into the wallet.
        RegistrationRequest registrationRequest = new RegistrationRequest("1appUser");
        registrationRequest.setAffiliation("org1.department1");
        registrationRequest.setEnrollmentID("1appUser");
        String enrollmentSecret = caClient.register(registrationRequest, admin);
        Enrollment enrollment = caClient.enroll("1appUser", enrollmentSecret);
        Identity user = Identities.newX509Identity("Org1MSP", adminIdentity.getCertificate(), adminIdentity.getPrivateKey());
        wallet.put("1appUser", user);
        System.out.println("Successfully enrolled user \"1appUser\" and imported it into the wallet");

    }

    public  Owner save(Owner ownerToRegister) throws Exception {

        HashMap<String, Object> settings = RegisterOwnerServiceImpl.init();
        // Check to see if we've already enrolled the user.
        Wallet wallet = (Wallet) settings.get("wallet");
        if (wallet.get(ownerToRegister.getUsername()) != null) {
            throw new RuntimeException("already exists in the wallet");
        }



        // Register the user, enroll the user, and import the new identity into the wallet.
        RegistrationRequest registrationRequest = new RegistrationRequest(ownerToRegister.getUsername());
        registrationRequest.setAffiliation("org1.department1");
        registrationRequest.setEnrollmentID(ownerToRegister.getUsername());
        HFCAClient caClient = (HFCAClient) RegisterOwnerServiceImpl.init().get("caClient");
        User admin = (User)settings.get("admin");
        String enrollmentSecret = caClient.register(registrationRequest, admin);
        Enrollment enrollment = caClient.enroll(ownerToRegister.getUsername(), enrollmentSecret);
        X509Identity adminIdentity = (X509Identity)settings.get("adminIdentity");
        Identity user = Identities.newX509Identity("Org1MSP", adminIdentity.getCertificate(), adminIdentity.getPrivateKey());
        wallet.put(ownerToRegister.getId().toString(), user);
        ownerToRegister.setAffiliation(registrationRequest.getAffiliation());
        log.debug("Successfully enrolled user "+ownerToRegister.getUsername()+" and imported it into the wallet");
        return iOwnerDao.save(ownerToRegister);
    }

    public void remove(Owner ownerToRemove) throws Exception {
            HashMap<String, Object> settings = RegisterOwnerServiceImpl.init();
            // Check to see if we've already enrolled the user.
            Wallet wallet = (Wallet) settings.get("wallet");
            if (wallet.get(ownerToRemove.getId()) == null) {
                throw new RuntimeException("doesn't exists in the wallet");
            }

            wallet.remove(ownerToRemove.getId());
            log.debug("Successfully removed user "+ownerToRemove.getUsername()+" from the wallet");
            iOwnerDao.deleteById(ownerToRemove.getId());
    }
}
