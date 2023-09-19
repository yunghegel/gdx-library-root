package org.yunghegel.gdx.ui.widgets.viewport.events;

import org.yunghegel.gdx.ui.widgets.viewport.ViewportWidget;

public class ViewportEventListener {

    public boolean handle(ViewportEvent event , ViewportWidget widget){
        if(event instanceof ViewportExitedEvent exitedEvent) {
            System.out.println("handling event " +event.getClass().getSimpleName());

            return exited(exitedEvent);
        }
        if(event instanceof ViewportEnteredEvent enteredEvent) {
            System.out.println("handling event " +event.getClass().getSimpleName());

            return entered(enteredEvent);
        }
        if(event instanceof ViewportResizedEvent resizedEvent) {
            return resized(resizedEvent, resizedEvent.width, resizedEvent.height);
        }
        if(event instanceof GameViewportAppliedEvent gameViewportAppliedEvent) {
            return gameViewportApplied(gameViewportAppliedEvent);
        }
        if(event instanceof StageViewportAppliedEvent stageViewportAppliedEvent) {
            return stageViewportApplied(stageViewportAppliedEvent);
        }

        return false;
    }


    public boolean exited(ViewportEvent event){
        return false;
    }

    public boolean entered(ViewportEvent event){
        return false;
    }

    public boolean resized(ViewportEvent event, int width, int height){
        return false;
    }

    public boolean gameViewportApplied(ViewportEvent event){
        return false;
    }

    public boolean stageViewportApplied(ViewportEvent event){
        return false;
    }

}
