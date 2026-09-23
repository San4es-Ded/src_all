vanilla_model.ALL:setVisible(false)
vanilla_model.HELD_ITEMS:setVisible(false)
local m=models.MODELNAME
local t=0
events.RENDER:register(function(delta)
 t=world.getTime()+delta
 local breathe=math.sin(t*0.12)*0.35
 m.Zhirskirovka_Body:setPos(0,breathe,-3)
 m.Zhirskirovka_Head:setPos(0,breathe*1.4,-3)
 m.Zhirskirovka_Head:setRot(math.sin(t*0.035)*2,math.sin(t*0.025)*5,math.sin(t*0.04)*1.5)
 m.Zhirskirovka_LeftArm:setRot(math.sin(t*0.09)*3,0,-8)
 m.Zhirskirovka_RightArm:setRot(math.sin(t*0.09+1.4)*3,0,8)
 m.Chair:setRot(0,math.sin(t*0.018)*1.2,0)
end)
