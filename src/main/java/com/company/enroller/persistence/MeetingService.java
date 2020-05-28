package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

    DatabaseConnector connector;
    
    @Autowired
    ParticipantService participantService;

    public MeetingService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Meeting> getAll() {
        String hql = "FROM Meeting";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }
    
    public Meeting findById(long meetingId) {
    	return (Meeting) connector.getSession().get(Meeting.class, meetingId);
    }
    
    public Meeting addMeeting(Meeting meeting) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(meeting);
        transaction.commit();
        return meeting;
    }

    public void deleteMeeting(Meeting meeting) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(meeting);
        transaction.commit();
    }
    
    public Meeting updateMeeting(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(meeting);
		transaction.commit();
		return meeting;
	}
    
    public Collection<Participant> getMeetings(long id) {
		Meeting meeting = findById(id);
		return meeting.getParticipants();
	}

    public Meeting addParticipant(long id, Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		Meeting meeting = findById(id);
		meeting.addParticipant(participant);
		connector.getSession().merge(meeting);
		transaction.commit();
		return meeting;
	}

	public Meeting deleteParticipant(long id, Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		Meeting meeting = findById(id);
		meeting.removeParticipant(participant);
		connector.getSession().merge(meeting);
		transaction.commit();
		return meeting;
	}

}

