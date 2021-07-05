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


    @Value("${hlf.properties.cert.pem-file}")
    private String pemFile;

    @Value("${hlf.caClient-url}")
    private String caClientUrl;

    @Value("${hlf.couchdb-wallet-url}")
    private String walletUrl;

    @Value("${hlf.couchdb-wallet-name}")
    private String walletName;

    @Value("${hlf.admin.username}")
    private String adminUsername;

    @Value("${hlf.admin.password}")
    private String adminPassword;

    @Value("${hlf.admin.email}")
    private String adminEmail;

    @Value("${hlf.mspId}")
    private String mspId;



    @Override
    public void run() throws Exception {

        // Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put("pemFile", this.pemFile);
//        props.put("pemFile", "/my-app/ca.org1.example.com-cert.pem");
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance(this.caClientUrl, props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newCouchDBWallet(new URL(this.walletUrl),this.walletName);

        // Check to see if we've already enrolled the admin user.
        if (wallet.get(this.adminUsername) != null) {
            log.warn("An identity for the admin user \""+this.adminUsername+"\" already exists in the wallet");
            if(!iUserDao.existsByUsername("admin")){
                User userToSave =new User(ObjectId.get().toString(),
                        this.adminUsername,
                        this.adminEmail,
                        passwordEncoder.encode(this.adminPassword),
                        Collections.singleton(iRoleDao.findByName(ERole.ROLE_ADMIN).get()));

                iUserDao.save(userToSave);
            }
            return;
        }

        // Enroll the admin user, and import the new identity into the wallet.
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost("localhost");
        enrollmentRequestTLS.setProfile("tls");
        Enrollment enrollment = caClient.enroll(this.adminUsername, this.adminPassword, enrollmentRequestTLS);
        Identity user = Identities.newX509Identity(this.mspId, enrollment);

        User userToSave =new User(ObjectId.get().toString(),
                this.adminUsername,
                this.adminEmail,
                passwordEncoder.encode(this.adminPassword),
                Collections.singleton(iRoleDao.findByName(ERole.ROLE_ADMIN).get()));

        wallet.put(userToSave.getUsername(), user);
        iUserDao.save(userToSave);

        log.info("Successfully enrolled user \""+this.adminUsername+"\" and imported it into the wallet");
    }
}
