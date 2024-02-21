package com.example.service;

import com.example.dto.AuthDTO;
import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.RegistrationDTO;
import com.example.entity.EmailSendHistoryEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.SmsHistoryEntity;
import com.example.enums.AppLanguage;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadException;
import com.example.repository.EmailSendHistoryRepository;
import com.example.repository.ProfileRepository;
import com.example.repository.SmsHistoryRepository;
import com.example.util.JWTUtil;
import com.example.util.MDUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
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
    @Autowired
    private ResourceBundleMessageSource resourceBundleMessageSource;
    @Autowired
    private ResourceBundleService resourceBundleService;

    public ProfileDTO auth(AuthDTO profile, AppLanguage appLanguage) { // login
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndPassword(profile.getEmail(),
                MDUtil.encode(profile.getPassword()));

        if (optional.isEmpty()) {
            log.warn("profile not fount {}", profile.getEmail());
            throw new AppBadException(resourceBundleService.getMessage("email.password.wrong", appLanguage));
        }

        ProfileEntity entity = optional.get();
        if (!entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new AppBadException(resourceBundleService.getMessage("email.password.wrong", appLanguage));
        }

        ProfileDTO dto = new ProfileDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setRole(entity.getRole());
        dto.setJwt(JWTUtil.encode(entity.getEmail(), entity.getRole()));

        return dto;
    }


    public Boolean registration(RegistrationDTO dto, AppLanguage language) {
        Pattern patternGmail = Pattern.compile("^[a-zA-Z0-9._%+-]+@(mail\\.ru|gmail\\.com)$");
        Pattern patternPassword = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");

        if (dto.getEmail() == null || dto.getName() == null
                || dto.getSurname() == null || dto.getPassword() == null) {
            log.warn("you have not filled all of the spaces provided{}", dto.getEmail());
            throw new AppBadException(resourceBundleService.getMessage("you.have.not", language));
        }
        if (!patternGmail.matcher(dto.getEmail()).matches()) {
            log.warn("Email was entered incorrectly{}", dto.getEmail());
            throw new AppBadException(resourceBundleService.getMessage("email.was.entered ", language));
        } else if (!patternPassword.matcher(dto.getPassword()).matches()) {
            log.warn("The password was entered incorrectly{}", dto.getEmail());
            throw new AppBadException(resourceBundleService.getMessage("the.password.was", language));
        }
//        //================ 1 minutda faqat 1 sms ga ruxsat qilib qo'yildi ================\\
        LocalDateTime from = LocalDateTime.now().minusMinutes(1);
        LocalDateTime to = LocalDateTime.now();
//        if (smsHistoryRepository.countSendSms(dto.getPhone(), from, to) >= 1) {
//            throw new AppBadException(resourceBundleService.getMessage("to.many.attempt", language));
//        }
//
//        Optional<ProfileEntity> optional = profileRepository.findByPhone(dto.getPhone());
//        if (optional.isPresent()) {
//            if (optional.get().getStatus().equals(ProfileStatus.REGISTRATION)) {
//                profileRepository.delete(optional.get());
//            } else {
//                log.warn("phone exists{}", dto.getPhone());
//                throw new AppBadException(resourceBundleService.getMessage("phone.exist", language));
//            }
//        }
       // ========== MAIL YOKI GMAIL GA HABAR JONATISHNI TEKSHIRISH 1 MINDA 3 TA HABAR JONATISH MUMKN ==============\\

        if (emailSendHistoryRepository.countSendEmail(dto.getEmail(), from, to) >= 3) {
            throw new AppBadException("To many attempt. Please try after 1 minute.");
        }
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            if (optional.get().getStatus().equals(ProfileStatus.REGISTRATION)) {
                profileRepository.delete(optional.get());
            } else {
                throw new AppBadException("Email exists");
            }
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MDUtil.encode(dto.getPassword()));
        entity.setPhone(dto.getPhone());
        entity.setStatus(ProfileStatus.REGISTRATION);
        entity.setRole(ProfileRole.ROLE_USER);
        profileRepository.save(entity);
        /**
         * sms kod yuborish
         */
//        String code = RandomUtil.getRandomSmsCode();
//        smsServerService.send(dto.getPhone(), "kun.uz test verification code: ", code);

//        send verification code (email/sms)
        /*
         * email kod yuborish
         */
        sendEmailMessage(dto, entity,language);     //  ==============> email jonaish uchun
        return true;
    }


    private void sendEmailMessage(RegistrationDTO dto, ProfileEntity entity,AppLanguage language) {
        String jwt = JWTUtil.encodeForEmail(entity.getId());
        String text = getButtonLink(entity, jwt);
        EmailSendHistoryEntity emailSendHistoryEntity = new EmailSendHistoryEntity();
        emailSendHistoryEntity.setEmail(dto.getEmail());
        emailSendHistoryEntity.setMessage(jwt);
        emailSendHistoryEntity.setStatus(ProfileStatus.REGISTRATION);
        emailSendHistoryEntity.setCreatedData(LocalDateTime.now());
        emailSendHistoryRepository.save(emailSendHistoryEntity);
        mailSender.sendEmail(dto.getEmail(), resourceBundleService.getMessage("complete.registration", language), text);
    }


    public ProfileDTO smsVerification(String phone, String code,AppLanguage language) {
        LocalDateTime from = LocalDateTime.now().minusMinutes(1);
        LocalDateTime to = LocalDateTime.now();
        Optional<ProfileEntity> byPhone = profileRepository.findByPhone(phone);
        if (byPhone.isEmpty()) {
            log.warn("phone not fount{}", phone);
            throw new AppBadException(resourceBundleService.getMessage("phone.not.fount",language));
        }
        if (!byPhone.get().getStatus().equals(ProfileStatus.REGISTRATION)) {
            log.warn("Profile in wrong status");
            throw new AppBadException(resourceBundleService.getMessage("profile.in.wrong.status",language));
        }
        Optional<SmsHistoryEntity> checkCode = smsHistoryRepository.getPhoneAndCode(phone, code, from, to);
        if (checkCode.isEmpty()) {
            log.warn("error, please try again!");
            throw new AppBadException(resourceBundleService.getMessage("sms.timed.out",language));
        }
        profileRepository.updateStatusActive(phone, ProfileStatus.ACTIVE);
        ProfileEntity profileEntity = byPhone.get();
        saveHistory(phone, code, ProfileStatus.ACTIVE);
        ProfileDTO dto = new ProfileDTO();
        dto.setRole(profileEntity.getRole());
        dto.setName(profileEntity.getName());
        dto.setSurname(profileEntity.getSurname());
        dto.setJwt(JWTUtil.encode(profileEntity.getId(), profileEntity.getRole()));
        return dto;
    }

    public String emailVerification(String jwt,AppLanguage language) {
        try {
            JwtDTO jwtDTO = JWTUtil.decode(jwt);
            Optional<ProfileEntity> optional = profileRepository.findById(jwtDTO.getId());
            if (optional.isEmpty()) {
                log.warn("Profile not found");
                throw new AppBadException(resourceBundleService.getMessage("phone.not.fount",language));
            }
            ProfileEntity entity = optional.get();
            if (!entity.getStatus().equals(ProfileStatus.REGISTRATION)) {
                log.warn("Profile in wrong status");
                throw new AppBadException(resourceBundleService.getMessage("profile.in.wrong.status",language));
            }
            profileRepository.updateStatus(entity.getId(), ProfileStatus.ACTIVE);
            EmailSendHistoryEntity emailSendHistoryEntity = new EmailSendHistoryEntity();
            emailSendHistoryEntity.setStatus(ProfileStatus.ACTIVE);
            emailSendHistoryEntity.setEmail(optional.get().getEmail());
            emailSendHistoryEntity.setMessage(jwt);
            emailSendHistoryRepository.save(emailSendHistoryEntity);


        } catch (JwtException e) {
            log.warn("Please tyre again.");
            throw new AppBadException(resourceBundleService.getMessage("please.tyre.again",language));
        }
        return "Success";
    }

    public void saveHistory(String phone, String code, ProfileStatus profileStatus) {
        SmsHistoryEntity entity = new SmsHistoryEntity();
        entity.setStatus(profileStatus);
        entity.setPhone(phone);
        entity.setMessage(code);
        entity.setCreatedDate(LocalDateTime.now());
        smsHistoryRepository.save(entity);
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
