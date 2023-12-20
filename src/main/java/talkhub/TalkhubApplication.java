package talkhub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import talkhub.model.User;
import talkhub.model.enums.Role;
import talkhub.repository.UserRepository;
import talkhub.util.CategoryConverter;

@SpringBootApplication
public class TalkhubApplication implements CommandLineRunner {
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(TalkhubApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(userRepository.findByLogin("moder1")==null){
			User moder1 = new User();
			moder1.setLogin("moder1");
			moder1.setPassword(encoder.encode("moder1"));
			moder1.setRole(Role.MODERATOR);

			userRepository.save(moder1);
		}
		if(userRepository.findByLogin("moder2")==null){
			User moder2 = new User();
			moder2.setLogin("moder2");
			moder2.setPassword(encoder.encode("moder2"));
			moder2.setRole(Role.MODERATOR);

			userRepository.save(moder2);
		}
	}
}
