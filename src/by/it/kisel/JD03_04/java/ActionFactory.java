package by.it.kisel.JD03_04.java;

import javax.servlet.http.HttpServletRequest;

class ActionFactory {
    ActionCommand defineCommand(HttpServletRequest request) {
        ActionCommand current = new CommandError();
        // извлечение имени команды из запроса ?command=action_name
        String action = request.getParameter("command");
        // получение объекта, соответствующего команде
        if (action != null && !action.isEmpty()) {
            try {
                Action currentEnum = Action.valueOf(action.toUpperCase());
                current = currentEnum.getCurrentCommand();
            } catch (IllegalArgumentException e) {
                request.setAttribute(Action.msgError, "<b>Unknown command: "+action+"</b>");
                request.setAttribute(Action.msgMessage, "<b>Unknown command: "+action+"</b>");
            }
        }
        //если команда не найдена, то вернем пустую.
        //иначе вернем команду для выполнения
        return current;
    }
}