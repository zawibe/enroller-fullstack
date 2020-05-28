package com.company.enroller.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity<Meeting>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> addMeeting(@RequestBody Meeting newMeeting) {
		meetingService.addMeeting(newMeeting);
		return new ResponseEntity<Meeting>(newMeeting, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting foundMeeting) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		} else {
			meetingService.updateMeeting(meeting);
			return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity("A meeting " + id + " doesn't exist.", HttpStatus.NOT_FOUND);
		} else {
			meetingService.deleteMeeting(meeting);
			return new ResponseEntity("A meeting  " + meeting.getTitle() + " has been deleted.", HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(@PathVariable("id") long id) {
		if (meetingService.findById(id) == null) {
			return new ResponseEntity("A meeting " + id + " doesn't exist.", HttpStatus.NOT_FOUND);
		} else {
			Collection<Participant> participants = meetingService.getMeetings(id);
			return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipant(@PathVariable("id") long id, @RequestBody String foundParticipant) {
		Participant participant = participantService.findByLogin(foundParticipant);
		if (participant == null) {
			return new ResponseEntity("A participant " + foundParticipant + " doesn't exist.", HttpStatus.NOT_FOUND);
		} else {
			meetingService.addParticipant(id, participant);
			return new ResponseEntity<Participant>(participant, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{id}/participants", method = RequestMethod.PUT)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") long id, @RequestBody String foundParticipant) {
		Participant participant = participantService.findByLogin(foundParticipant);
		Collection<Participant> participants = meetingService.getMeetings(id);
		if (participant == null) {
			return new ResponseEntity("A participant " + foundParticipant + " doesn't exist", HttpStatus.NOT_FOUND);
		} else {
			meetingService.deleteParticipant(id, participant);
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

}