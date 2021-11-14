package fr.raphoulfifou.cyan.config.options.storage;

import fr.raphoulfifou.cyan.CyanClientCore;
import fr.raphoulfifou.cyan.config.options.CyanOptions;

public class CyanOptionsStorage implements OptionStorage<CyanOptions>
{
    private final CyanOptions options = CyanClientCore.getOptions();

    @Override
    public CyanOptions getData()
    {
        return this.options;
    }

    @Override
    public void save()
    {
        this.options.writeChanges();
    }
    
}
