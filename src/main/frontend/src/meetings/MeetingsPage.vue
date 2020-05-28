<template>
  <div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length == 0">
               Brak zaplanowanych spotkań.
           </span>
    <h3 v-else>
      Zaplanowane zajęcia ({{ meetings.length }})
    </h3>

    <meetings-list :meetings="meetings"
                   :username="username"
                   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
  </div>
</template>

<script>
    import NewMeetingForm from "./NewMeetingForm";
    import MeetingsList from "./MeetingsList";

    export default {
        components: {NewMeetingForm, MeetingsList},
        props: ['username'],
        data() {
            return {
                meetings: []
            };
        },
         methods: {
        	
            getMeetings() {
                this.$http.get('meetings')
                .then(response => {this.meetings = response.body});
            },
            
            addNewMeeting(meeting) {
                this.$http.post('meetings', meeting)
                .then(response => this.meetings.push(response.body));
            	this.getMeetings()
            },
            
            deleteMeeting(meeting) {
                this.$http.delete(`meetings/${meeting.id}`, meeting);
                this.meetings.splice(this.meetings.indexOf(meeting), 1);
            },
            getMeetingsParticipants() {
                for (meeting in this.meetings) {
                    this.$http.get(`meetings/'+meeting.id+'/participants`)
                    .then(response => {meeting.participants = response.body});
                }
            },  
            
            addMeetingParticipant(meeting) {
                this.$http.post(`meetings/${meeting.id}/participants`, this.username)
                .then(response => meeting.participants.push(response.body));
            },
            
            removeMeetingParticipant(meeting) {
                this.$http.put(`meetings/${meeting.id}/participants`, this.username)
                .then(() => meeting.participants.splice(meeting.participants.indexOf(this.username), 1));
            },
            
        },
        
       mounted() {
           this.getMeetings();
           this.getMeetingsParticipants();
       },
    }
</script>
