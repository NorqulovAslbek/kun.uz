package com.example.service;

import com.example.dto.AuthDTO;
import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.RegistrationDTO;
import com.example.entity.EmailSendHistoryEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadException;
import com.example.repository.EmailSendHistoryRepository;
import com.example.repository.ProfileRepository;
import com.example.repository.SmsHistoryRepository;
import com.example.util.JWTUtil;
import com.example.util.MDUtil;
import com.example.util.RandomUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

@Service

public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MailSenderService mailSender;
    @Autowired
    private EmailSendHistoryRepository emailSendHistoryRepository;
    @Autowired
    private SmsServerService smsServerService;
    @Autowired
    private SmsHistoryRepository smsHistoryRepository;

    public ProfileDTO auth(AuthDTO profile) { // login
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndPassword(profile.getEmail(),
                MDUtil.encode(profile.getPassword()));

        if (optional.isEmpty()) {
            throw new AppBadException("Email or Password is wrong");
        }

        ProfileEntity entity = optional.get();
        if (!entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new AppBadException("Profile not active");
        }

        ProfileDTO dto = new ProfileDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setRole(entity.getRole());
        dto.setJwt(JWTUtil.encode(entity.getId(), entity.getRole()));

        return dto;
    }


    public Boolean registration(RegistrationDTO dto) {
        Pattern patternGmail = Pattern.compile("^[a-zA-Z0-9._%+-]+@(mail\\.ru|gmail\\.com)$");
        Pattern patternPassword = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");

        if (dto.getEmail() == null || dto.getName() == null
                || dto.getSurname() == null || dto.getPassword() == null) {
            throw new AppBadException("you have not filled all of the spaces provided!");
        }
        if (!patternGmail.matcher(dto.getEmail()).matches()) {
            throw new AppBadException("Email was entered incorrectly");
        } else if (!patternPassword.matcher(dto.getPassword()).matches()) {
            throw new AppBadException("The password was entered incorrectly");
        }
     //================ 1 minutda faqat 1 sms ga ruxsat qilib qo'yildi ================//
        LocalDateTime from = LocalDateTime.now().minusMinutes(1);
        LocalDateTime to = LocalDateTime.now();
        if (smsHistoryRepository.countSendSms(dto.getPhone(),from,to)>=1){
            throw new AppBadException("To many attempt. Please try after 1 minute.");
        }

        Optional<ProfileEntity> optional = profileRepository.findByPhone(dto.getPhone());
        if (optional.isPresent()) {
            if (optional.get().getStatus().equals(ProfileStatus.REGISTRATION)) {
                profileRepository.delete(optional.get());
            } else {
                throw new AppBadException("Email exists");
            }
        }

     //======================= MAIL YOKI GMAIL GA HABAR JONATISHNI TEKSHIRISH 1 MINDA 3 TA HABAR JONATISH MUMKN ==============\\

//        if (emailSendHistoryRepository.countSendEmail(dto.getEmail(), from, to) >= 3) {
//            throw new AppBadException("To many attempt. Please try after 1 minute.");
//        }
//
//        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
//        if (optional.isPresent()) {
//            if (optional.get().getStatus().equals(ProfileStatus.REGISTRATION)) {
//                profileRepository.delete(optional.get());
//            } else {
//                throw new AppBadException("Email exists");
//            }
//        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MDUtil.encode(dto.getPassword()));
        entity.setPhone(dto.getPhone());
        entity.setStatus(ProfileStatus.REGISTRATION);
        entity.setRole(ProfileRole.USER);
        profileRepository.save(entity);
        /**
         * sms kod yuborish
         */
        String code = RandomUtil.getRandomSmsCode();
        smsServerService.send(dto.getPhone(), "kun.uz test verification code: ", code);
//               send verification code (email/sms)
        /*
         * email kod yuborish
         */
//        sendEmailMessage(dto, entity);     //  ==============> email jonaish uchun
        return true;
    }


    private void sendEmailMessage(RegistrationDTO dto, ProfileEntity entity) {
        String jwt = JWTUtil.encodeForEmail(entity.getId());
        String text = getButtonLink(entity, jwt);
        EmailSendHistoryEntity emailSendHistoryEntity = new EmailSendHistoryEntity();
        emailSendHistoryEntity.setEmail(dto.getEmail());
        emailSendHistoryEntity.setMessage(jwt);
        emailSendHistoryEntity.setStatus(ProfileStatus.REGISTRATION);
        emailSendHistoryEntity.setCreatedData(LocalDateTime.now());
        emailSendHistoryRepository.save(emailSendHistoryEntity);
        mailSender.sendEmail(dto.getEmail(), "Complete registration", text);
    }

    public String emailVerification(String jwt) {
        try {
            JwtDTO jwtDTO = JWTUtil.decode(jwt);

            Optional<ProfileEntity> optional = profileRepository.findById(jwtDTO.getId());
            if (optional.isEmpty()) {
                throw new AppBadException("Profile not found");
            }
            ProfileEntity entity = optional.get();
            if (!entity.getStatus().equals(ProfileStatus.REGISTRATION)) {
                throw new AppBadException("Profile in wrong status");
            }
            profileRepository.updateStatus(entity.getId(), ProfileStatus.ACTIVE);
            EmailSendHistoryEntity emailSendHistoryEntity = new EmailSendHistoryEntity();
            emailSendHistoryEntity.setStatus(ProfileStatus.ACTIVE);
            emailSendHistoryEntity.setEmail(optional.get().getEmail());
            emailSendHistoryEntity.setMessage(jwt);
            emailSendHistoryRepository.save(emailSendHistoryEntity);


        } catch (JwtException e) {
            throw new AppBadException("Please tyre again.");
        }
        return "Success";
    }

    private static String getButtonLink(ProfileEntity entity, String jwt) {
        String text = """
                <h1 style="text-align: center">Hello %s</h1>
                <p style="background-color: indianred; color: white; padding: 30px">To complete registration please link to the following link</p>
                <a style=" background-color: #f44336;
                  color: white;
                  padding: 14px 25px;
                  text-align: center;
                  text-decoration: none;
                  display: inline-block;" href="http://localhost:8080/auth/verification/email/%s
                ">Click</a>
                <br>
                """;
        text = String.format(text, entity.getName(), jwt);
        return text;
    }


}
