*** Settings ***
Library         RequestsLibrary
Library         Collections
Library         DateTime
Library         FakerLibrary

*** Variables ***
# --- CONFIGURAÇÕES DE AMBIENTE (EXISTENTES) ---
${BASE_URL}      %{BASE_URL}
${public_key}    %{MERCADO_PAGO_PUBLIC_KEY}
# ${BASE_URL}             http://localhost:8080/api
${ENDPOINT_EVENT}       /event
${ENDPOINT_TICKET}      /event/{eventId}/ticket-type
${ENDPOINT_BUY_TICKET}  /ticket/buy
${ENDPOINT_UPDATE_TICKET}    /ticket-type/{ticketTypeId}
${ENDPOINT_DELETE_TICKET}    /ticket-type/{ticketTypeId}
${ENDPOINT_UPDATE_EVENT}    /event/{eventId}

# --- DADOS FIXOS DO EVENTO (EXISTENTES) ---
${FUTURE_DATE_INITIAL}  2030-10-10T18:00:00
${FUTURE_DATE_FINAL}    2030-10-10T21:00:00
${SUCCESS_LOCATION}     Sala Alpha
${SUCCESS_CAPACITY}     100

# --- NOVO ENDPOINT PARA PATCH DE EVENTO ---
${UPDATED_EVENT_NAME}        Evento Atualizado
${UPDATED_EVENT_LOCATION}    Sala Beta Atualizada
${UPDATED_EVENT_CAPACITY}    150
${UPDATED_EVENT_AGE_RESTRICTION}    16

# --- DATAS PARA TESTES ---
${PAST_DATE}                 2020-01-01T10:00:00
${CONFLICT_DATE_INITIAL}     2030-10-10T19:00:00
${CONFLICT_DATE_FINAL}       2030-10-10T22:00:00

# --- VARIÁVEIS PARA TESTES DE ATUALIZAÇÃO ---

${UPDATED_TICKET_NAME}        Ingresso Atualizado
${UPDATED_TICKET_PRICE}       200
${UPDATED_TICKET_CAPACITY}    80
${UPDATED_DATE_INITIAL}       2030-10-10T10:00:00
${UPDATED_DATE_FINAL}         2030-10-10T12:00:00

# --- VARIÁVEIS DE CONTROLE GLOBAL (EXISTENTES) ---
${CREATED_EVENT_ID}     ${EMPTY}

# --- NOVAS VARIÁVEIS PARA TICKETS ---
${GLOBAL_EVENT_ID}       ${EMPTY}
${GLOBAL_TICKET_NAME_1}  ${EMPTY}
${GLOBAL_TICKET_NAME_2}  ${EMPTY}

${uuid_inexistente}          f2f1e0d9-c8b7-4a65-83e2-1d5c7b9a4f3e
${uuid_invalido}             1234

# --- TAGS (EXISTENTES) ---
${TAG_ALL_TESTS}        POST_EVENT
${TAG_TICKET_TESTS}     TICKET_CRUD

# --- STATUS CODES (EXISTENTES) ---
${STATUS_200}           200
${STATUS_201}           201
${STATUS_204}           204
${STATUS_400}           400
${STATUS_404}           404
${STATUS_409}           409
${STATUS_422}           422