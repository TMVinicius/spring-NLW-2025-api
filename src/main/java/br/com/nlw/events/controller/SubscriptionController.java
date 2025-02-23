package br.com.nlw.events.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.nlw.events.dto.ErrorMessage;
import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.exception.EventNotFoundException;
import br.com.nlw.events.exception.SubscriptionConflictException;
import br.com.nlw.events.exception.UserIndicatorNotFoundException;
import br.com.nlw.events.model.User;
import br.com.nlw.events.service.SubscriptionService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SubscriptionController {

	
	private final SubscriptionService service;
	
	@PostMapping({"/subscription/{prettyName}","/subscription/{prettyName}/{userId}"})
	public ResponseEntity<?> createSubscription(@PathVariable String prettyName, 
	@RequestBody User subscriber, @PathVariable(required=false) Integer userId){
		
	try {
		
	
		SubscriptionResponse res = service.createNewSubscription(prettyName, subscriber, userId);
		if(res != null) {
			return ResponseEntity.ok(res);
		}
	}	catch(EventNotFoundException ex) {
			return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
	}	
		catch(SubscriptionConflictException ex) {
			return ResponseEntity.status(409).body(new ErrorMessage(ex.getMessage()));	
	}
		catch(UserIndicatorNotFoundException ex ) {
			return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
		}
 	
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/subscription/{prettyName}/ranking")
    public ResponseEntity<?> generateRaningByEvent(@PathVariable String prettyName){
        try{
            return ResponseEntity.ok(service.getCompleteRanking(prettyName));
        }catch(EventNotFoundException e){
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }
    }

    @GetMapping("/subscription/{prettyName}/ranking/{userId}")
    public ResponseEntity<?> generateRankingByEventAndUser(@PathVariable String prettyName, @PathVariable Integer userId){
        try{
            return ResponseEntity.ok(service.getRankingByUser(prettyName, userId));
        }catch(Exception ex){
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        }
    } 
	
	
}
