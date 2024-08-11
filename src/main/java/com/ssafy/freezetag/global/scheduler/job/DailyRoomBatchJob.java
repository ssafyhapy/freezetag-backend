package com.ssafy.freezetag.global.scheduler.job;

import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.service.MailService;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.entity.Room;
import com.ssafy.freezetag.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.ssafy.freezetag.domain.common.constant.MailConstant.MEMORYBOX_MAIL_TITLE;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyRoomBatchJob implements Job {
    private final RoomRepository roomRepository;
    private final MailService mailService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 30일이 지난 룸을 조회
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        List<Room> appliedRooms = roomRepository.findByCreatedDateBefore(thirtyDaysAgo);

        // 각 룸에 대해 이메일 발송
        for (Room room : appliedRooms) {
            List<MemberRoom> memberRooms = room.getMemberRooms();
            for (MemberRoom memberRoom : memberRooms) {
                Member member = memberRoom.getMember();
                try {
                    mailService.sendEmail(member.getMemberProviderEmail(), MEMORYBOX_MAIL_TITLE, null);
                } catch (Exception e) {
                    log.error("{} 님을 찾지 못했습니다. 아이디 : {}",member.getMemberName(), member.getId());
                }
            }
            room.updateSentStatus(true);
        }
    }
}

