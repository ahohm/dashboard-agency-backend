package com.akkurad.dashboardagencybackend.service.impl;

import com.akkurad.dashboardagencybackend.dao.IRoleDao;
import com.akkurad.dashboardagencybackend.dao.IUserDao;
import com.akkurad.dashboardagencybackend.model.ERole;
import com.akkurad.dashboardagencybackend.model.User;
import com.akkurad.dashboardagencybackend.security.service.UserDetailsServiceImpl;
import com.akkurad.dashboardagencybackend.service.IEnrollAdminService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Collections;
import java.util.Properties;

@Service
@Slf4j
public class EnrollAdminServiceImpl implements IEnrollAdminService {

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    @Autowired
    private IUserDao iUserDao;
    @Autowired
    private IRoleDao iRoleDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsServiceImpl uSerDetailsService;


    @Value("${hlf.org1.properties.cert.pem-file}")
    private String pemFile_org1;
    @Value("${hlf.org1.caClient-url}")
    private String caClientUrl_org1;
    @Value("${hlf.org1.couchdb-wallet-url}")
    private String walletUrl_org1;
    @Value("${hlf.org1.couchdb-wallet-name}")
    private String walletName_org1;
    @Value("${hlf.org1.admin.username}")
    private String adminUsername_org1;
    @Value("${hlf.org1.admin.password}")
    private String adminPassword_org1;
    @Value("${hlf.org1.admin.email}")
    private String adminEmail_org1;
    @Value("${hlf.org1.mspId}")
    private String mspId_org1;


    @Value("${hlf.org2.properties.cert.pem-file}")
    private String pemFile_org2;
    @Value("${hlf.org2.caClient-url}")
    private String caClientUrl_org2;
    @Value("${hlf.org2.couchdb-wallet-url}")
    private String walletUrl_org2;
    @Value("${hlf.org2.couchdb-wallet-name}")
    private String walletName_org2;
    @Value("${hlf.org2.admin.username}")
    private String adminUsername_org2;
    @Value("${hlf.org2.admin.password}")
    private String adminPassword_org2;
    @Value("${hlf.org2.admin.email}")
    private String adminEmail_org2;
    @Value("${hlf.org2.mspId}")
    private String mspId_org2;



    @Override
    public void runOrg1() throws Exception {

        // Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put("pemFile", this.pemFile_org1);
//        props.put("pemFile", "/my-app/ca.org1.example.com-cert.pem");
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance(this.caClientUrl_org1, props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newCouchDBWallet(new URL(this.walletUrl_org1),this.walletName_org1);

        // Check to see if we've already enrolled the admin user.
        if (wallet.get(this.adminUsername_org1) != null) {
            log.warn("An identity for the admin user \""+this.adminUsername_org1 +"\" already exists in the wallet");
            if(!iUserDao.existsByUsername("admin")){
                User userToSave =new User(ObjectId.get().toString(),
                        this.adminUsername_org1,
                        this.adminEmail_org1,
                        passwordEncoder.encode(this.adminPassword_org1),
                        Collections.singleton(iRoleDao.findByName(ERole.ROLE_ADMIN).get()));

                iUserDao.save(userToSave);
            }
            return;
        }

        // Enroll the admin user, and import the new identity into the wallet.
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost("localhost");
        enrollmentRequestTLS.setProfile("tls");
        Enrollment enrollment = caClient.enroll(this.adminUsername_org1, this.adminPassword_org1, enrollmentRequestTLS);
        Identity user = Identities.newX509Identity(this.mspId_org1, enrollment);

        User userToSave =new User(ObjectId.get().toString(),
                this.adminUsername_org1,
                this.adminEmail_org1,
                passwordEncoder.encode(this.adminPassword_org1),
                Collections.singleton(iRoleDao.findByName(ERole.ROLE_ADMIN).get()));

        wallet.put(userToSave.getUsername(), user);
        iUserDao.save(userToSave);

        log.info("Successfully enrolled user \""+this.adminUsername_org1 +"\" and imported it into the wallet");

    }

    @Override
    public void runOrg2() throws Exception {

// Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put("pemFile", this.pemFile_org2);
//        props.put("pemFile", "/my-app/ca.org1.example.com-cert.pem");
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance(this.caClientUrl_org2, props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newCouchDBWallet(new URL(this.walletUrl_org2),this.walletName_org2);

        // Check to see if we've already enrolled the admin user.
        if (wallet.get(this.adminUsername_org2) != null) {
            log.warn("An identity for the admin user \""+this.adminUsername_org2 +"\" already exists in the wallet");
            if(!iUserDao.existsByUsername("admin")){
                User userToSave =new User(ObjectId.get().toString(),
                        this.adminUsername_org2,
                        this.adminEmail_org2,
                        passwordEncoder.encode(this.adminPassword_org2),
                        Collections.singleton(iRoleDao.findByName(ERole.ROLE_ADMIN).get()));

                iUserDao.save(userToSave);
            }
            return;
        }

        // Enroll the admin user, and import the new identity into the wallet.
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost("localhost");
        enrollmentRequestTLS.setProfile("tls");
        Enrollment enrollment = caClient.enroll(this.adminUsername_org2, this.adminPassword_org2, enrollmentRequestTLS);
        Identity user = Identities.newX509Identity(this.mspId_org2, enrollment);

        User userToSave =new User(ObjectId.get().toString(),
                this.adminUsername_org2,
                this.adminEmail_org2,
                passwordEncoder.encode(this.adminPassword_org2),
                Collections.singleton(iRoleDao.findByName(ERole.ROLE_ADMIN).get()));

        wallet.put(userToSave.getUsername(), user);
        iUserDao.save(userToSave);

        log.info("Successfully enrolled user \""+this.adminUsername_org2 +"\" and imported it into the wallet");

    }
}
