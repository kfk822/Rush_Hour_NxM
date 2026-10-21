package rush.hour.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GameEventPublisherTest {

    @Mock
    private GameEventListener firstListener;

    @Mock
    private GameEventListener secondListener;

    private GameEventPublisher gameEventPublisher;

    @BeforeEach
    void setUp() {
        gameEventPublisher = new GameEventPublisher();
    }

    @Test
    @DisplayName("Should deliver event to subscriber when the event is published")
    void testPublishShouldNotifyListenerWhenSubscribed() {
        gameEventPublisher.subscribe(firstListener);
        GameEvent event = new GameEvent(GameEventType.DEBUG, "Debug event");

        gameEventPublisher.publish(event);

        verify(firstListener, times(1)).onEvent(event);
    }

    @Test
    @DisplayName("Should notify all subscribed listeners when multiple listeners exist")
    void testPublishShouldNotifyAllListenersWhenMultipleSubscribersExist() {
        gameEventPublisher.subscribe(firstListener);
        gameEventPublisher.subscribe(secondListener);
        GameEvent event = new GameEvent(GameEventType.DEBUG, "Debug event");

        gameEventPublisher.publish(event);

        verify(firstListener, times(1)).onEvent(event);
        verify(secondListener, times(1)).onEvent(event);
    }

    @Test
    @DisplayName("Should not notify listener when listener is not subscribed")
    void testPublishShouldNotNotifyListenerWhenNotSubscribed() {
        GameEvent event = new GameEvent(GameEventType.DEBUG, "Debug event");

        gameEventPublisher.publish(event);

        verify(firstListener, never()).onEvent(event);
    }
}
