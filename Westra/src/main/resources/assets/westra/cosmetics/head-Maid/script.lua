-- Mirror @MagicLab --

--hide vanilla model
vanilla_model.PLAYER:setVisible(false)

--hide vanilla model
vanilla_model.PLAYER:setVisible(false)

local smoothie = require("Smoothie")

local smoothHead = smoothie:newSmoothHead(models.model.root.Waist.Head)

smoothHead:setStrength(0.65) 

local boobs = smoothie:newPhysicalBody(models.model.root.Waist.Body.chest.tits)