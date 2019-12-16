package bot.tools.kudaGo;

public class KudaGo {

    private boolean citySelected;
    public boolean isCitySelected() {
        return citySelected;
    }

    private String city;
    public String getAnswer(String message){
        if(!citySelected) {
            city = convertMessageToCity(message);
            if(citySelected) return "Вы выбрали город " + message;
            else return "Выберите город из предложенных";
        }
        return "oooooooo";
    }

    private String convertMessageToCity(String message){
        citySelected = true;
        switch (message){
            case "Екатеринбург": return "ekb";
            case "Красноярск": return "krasnoyarsk";
            case "Краснодар": return "krd";
            case "Казань": return "kzn";
            case "Москва": return "msk";
            case "Нижний Новгород": return "nnv";
            case "Новосибирск": return "nsk";
            case "Сочи": return "sochi";
            case "Санкт-Петербург": return "spb";
            default:
                citySelected = false;
                return "";
        }
    }
}
