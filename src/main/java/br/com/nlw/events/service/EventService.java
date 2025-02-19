package br.com.nlw.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nlw.events.model.Event;
import br.com.nlw.events.repo.EventRepo;

@Service
public class EventService {

	@Autowired
	private EventRepo eventRepo;
	
	public Event addNewEvent(Event event) {
		
		event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));
		return eventRepo.save(event);
	}
	
}
