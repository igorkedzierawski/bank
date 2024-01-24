package zet.kedzieri.bank;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import zet.kedzieri.bank.config.SensitiveDataCipher;
import zet.kedzieri.bank.config.ServerPasswordEncoder;
import zet.kedzieri.bank.domain.auth.loginsession.LoginSessionRepository;
import zet.kedzieri.bank.domain.auth.loginsession.LoginSessionService;
import zet.kedzieri.bank.domain.auth.passwordvariant.PasswordVariantRepository;
import zet.kedzieri.bank.domain.auth.passwordvariant.PasswordVariantService;
import zet.kedzieri.bank.domain.bank.account.Account;
import zet.kedzieri.bank.domain.bank.account.AccountRepository;
import zet.kedzieri.bank.domain.bank.account.AccountService;
import zet.kedzieri.bank.domain.client.Client;
import zet.kedzieri.bank.domain.client.ClientRepository;
import zet.kedzieri.bank.domain.client.ClientService;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

	@Bean
	public CommandLineRunner clr(
			SensitiveDataCipher sdc,
			ClientRepository cr,
			ClientService cs,
			AccountRepository ar,
			AccountService as,
			LoginSessionRepository lsr,
			LoginSessionService lss,
			PasswordVariantRepository pvr,
			PasswordVariantService pvs
	) {
		return x -> {
			Client client;
			Account account;

			long t1 = System.nanoTime();

			client = cr.save(new Client("Jan", "Kowalski", "123", sdc.encrypt("jan-id555666")));
			account = ar.save(new Account(client, "123456", 100_00, sdc.encrypt("jan-card4422111")));
			pvs.upsertPasswordVariants_unchecked(client, "a".repeat(12));
			lss.rotateLoginSessionPasswordVariant(client);

			client = cr.save(new Client("Adam", "Nowak", "321", sdc.encrypt("adam-id999888")));
			account = ar.save(new Account(client, "654321", 200_00, sdc.encrypt("adam-card7777333")));
			pvs.upsertPasswordVariants_unchecked(client, "b".repeat(12));
			lss.rotateLoginSessionPasswordVariant(client);

			long t2 = System.nanoTime();

			System.out.println("Initialized in "+(t2-t1)/1_000_000.0+"ms");
		};
	}

}
