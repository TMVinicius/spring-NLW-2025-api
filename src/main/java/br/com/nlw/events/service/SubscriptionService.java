package br.com.nlw.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nlw.events.exception.EventNotFoundException;
import br.com.nlw.events.exception.SubscriptionConflictException;
import br.com.nlw.events.model.Event;
import br.com.nlw.events.model.Subscription;
import br.com.nlw.events.model.User;
import br.com.nlw.events.repo.EventRepo;
import br.com.nlw.events.repo.SubscriptionRepo;
import br.com.nlw.events.repo.UserRepo;

@Service
public class SubscriptionService {

	@Autowired
	private EventRepo evtRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private SubscriptionRepo subRepo;
	
	public Subscription createNewSubscription(String eventName, User user) {
		
		Event evt = evtRepo.findByPrettyName(eventName);
		if (evt == null) {
			throw new EventNotFoundException("Evento "+eventName+" nao existe");
		}
		
		User userRec = userRepo.findByEmail(user.getEmail());
		if (userRec == null) {
			userRec = userRepo.save(user);
		}
		
		Subscription subs = new Subscription();
		subs.setEvent(evt);
		subs.setSubscriber(userRec);
		
		Subscription tmpSub = subRepo.findByEventAndSubscriber(evt, userRec);
		if (tmpSub != null) {
			throw new SubscriptionConflictException("Ja existe incricao para o usuario "+ userRec.getName());
		}
		
		Subscription res = subRepo.save(subs);
		return res;
	}
	
}
