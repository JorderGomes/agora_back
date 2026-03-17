package com.jorder.agora.service;

import com.jorder.agora.dto.EmailDTO;
import com.jorder.agora.model.Registration;
import com.jorder.agora.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationSchedulerService {

    private final RegistrationRepository registrationRepository;
    private final EmailService emailService;

    @Scheduled(fixedRate = 600000)
    public void checkAndSendNotifications() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(30);

        List<Registration> pending = registrationRepository.findPendingNotifications(start, end);

        log.info("Iniciando disparo de {} notificações", pending.size());

        if (!pending.isEmpty()) {
            pending.forEach(this::sendAndMarkAsNotified);
        }

    }

    private void sendAndMarkAsNotified(Registration reg) {
        try {
            var emailDTO = new EmailDTO(
                    reg.getUser().getEmail(),
                    "Evento iminente.",
                    "Seu evento "+ reg.getEvent().getTitle() +" começará em breve!"
            );
            emailService.sendMail(emailDTO);
            reg.setNotificationSent(true);
            registrationRepository.save(reg);
        } catch (Exception e) {
            log.error("Erro ao enviar notificação para usuário {}: {}", reg.getUser().getEmail(), e.getMessage());
        }
    }

}
