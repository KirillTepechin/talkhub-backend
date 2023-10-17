package talkhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import talkhub.dto.UserCredentialsDto;
import talkhub.dto.UserDto;
import talkhub.exception.WrongLoginOrPasswordException;
import talkhub.jwt.JwtProvider;
import talkhub.mapper.UserMapper;
import talkhub.model.User;
import talkhub.model.enums.Role;
import talkhub.repository.UserRepository;
import talkhub.util.FileUploadUtil;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileUploadUtil fileUploadUtil;

    public User findByLogin(String login){
        return userRepository.findByLogin(login);
    }
    @Transactional
    public void register(UserCredentialsDto dto) {
        if (findByLogin(dto.getLogin()) != null) {
            throw new IllegalArgumentException(String.format("Пользователь '%s' уже существует", dto.getLogin()));
        }
        User user = userMapper.fromUserCredentialsDto(dto);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }
    @Transactional
    public String auth(UserCredentialsDto dto) {
        User user = userRepository.findByLogin(dto.getLogin());
        if(user==null){
            throw new WrongLoginOrPasswordException();
        }
        if (encoder.matches(dto.getPassword(), user.getPassword())) {
            return jwtProvider.generateAccessToken(user);
        }
        else {
            throw new WrongLoginOrPasswordException();
        }
    }

    @Transactional(readOnly = true)
    public UserDto me(User user){
        return userMapper.toUserDto(user);
    }

    @Transactional
    public void editPhoto(MultipartFile file, User user){
        user.setPhoto(fileUploadUtil.uploadFile(file));
        userRepository.save(user);
    }

    @Transactional
    public void editCredentials(UserCredentialsDto dto, User user) {
        if(encoder.matches(dto.getPassword(), user.getPassword())){
            user.setPassword(encoder.encode(dto.getNewPassword()));
            if(dto.getLogin()!=null && !dto.getLogin().isBlank()){
                user.setLogin(dto.getLogin());
            }
            userRepository.save(user);
        }
        else{
            throw new WrongLoginOrPasswordException();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }

}
