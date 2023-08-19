const changeTime = (date) => {
  const time = new Date(date)
  if (time.getFullYear() > 1970)
  return time.toLocaleString('en-GB',{dateStyle:'medium', timeStyle:'short'})
  else return '-'
}

export {changeTime}