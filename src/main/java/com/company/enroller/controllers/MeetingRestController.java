package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/meetings")
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
	return new ResponseEntity(HttpStatus.NOT_FOUND);
	} 
	return new ResponseEntity<Meeting>(meeting, HttpStatus.OK); 
	}
	
	//1.3
	@RequestMapping(value = "", method = RequestMethod.POST) 
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting){
	Meeting foundMeeting = meetingService.findById(meeting.getId());
	if (foundMeeting != null) { 
	return new ResponseEntity<String>("Unable to register. Meeting with ID " +  
	meeting.getId() + "already exist", HttpStatus.CONFLICT);
	}
	meetingService.add(meeting);
	return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);		
	}	
	
	//2.1
	@RequestMapping(value = "/{id}/{login}", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipant(@PathVariable("id") long id, @PathVariable("login") String login) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
		
			meetingService.addParticipant(participant);
			meeting = meetingService.updateMeeting(meeting);
			return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
		}
	
	//2.2
	   @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
		public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
			Meeting meeting = meetingService.findById(id);
			if (meeting == null) { 
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				} 
			return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
		}
	   
	//3.1
	   @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
		public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
			Meeting meeting = meetingService.findById(id);
			if (meeting == null) { 
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				} 
			meetingService.delete(meeting);
			return new ResponseEntity<Meeting>(meeting, HttpStatus.NO_CONTENT);
		} 
	}