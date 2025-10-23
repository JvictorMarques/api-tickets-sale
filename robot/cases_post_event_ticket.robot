*** Settings ***
Resource        resource_api_event.robot
Resource        script_api_event.robot

Suite Setup     Setup Suite Para Tickets

*** Keywords ***
Setup Suite Para Tickets
    [Documentation]    Cria a sessao API para os testes de ticket
    Criar Sessao API

*** Test Cases ***
event-ticket-001 - Validar cadastro de novo evento com cadastro de novo tipo de ingresso para o mesmo
    [Tags]          TICKET_CRUD    SUCESSO    INICIAL
    [Documentation]    Cadastra evento e primeiro ingresso (capacidade 30)
    
    # 1. Cria evento base e captura ID
    ${event_id}=    Criar Evento Base Para Tickets
    Set Global Variable     ${GLOBAL_EVENT_ID}     ${event_id}

    # 2. Gera nome aleatório e payload do ticket
    ${ticket_name}=     Gerar Nome Ticket Aleatorio
    Set Global Variable     ${GLOBAL_TICKET_NAME_1}    ${ticket_name}

    &{payload}=     Criar Payload Ticket Basico    ${ticket_name}    ${120}    ${30}
    
    # 3. Executa POST do ticket
    ${response}=    Executar Post Ticket Para Evento    ${event_id}     ${payload}      ${201}
    
    # 4. Validações
    Validar Response Ticket Sucesso 201      ${response}     ${ticket_name}    ${event_id}


event-ticket-002 - Validar cadastro de novo tipo ingresso para o evento anterior com capacidade complementar
    [Tags]          TICKET_CRUD    SUCESSO    COMPLEMENTAR
    [Documentation]    Cadastra segundo ingresso (capacidade 20) - Total: 50/100
    
    # 1. Usa evento criado no cenário 001
    ${event_id}=    Set Variable    ${GLOBAL_EVENT_ID}
    
    # 2. Gera nome aleatório e payload do ticket
    ${ticket_name}=     Gerar Nome Ticket Aleatorio
    Set Global Variable     ${GLOBAL_TICKET_NAME_2}    ${ticket_name}

    &{payload}=     Criar Payload Ticket Basico    ${ticket_name}    ${240}    ${20}

    # 3. Executa POST do ticket
    ${response}=    Executar Post Ticket Para Evento    ${event_id}     ${payload}      ${201}
    
    # 4. Validações
    Validar Response Ticket Sucesso 201      ${response}     ${ticket_name}    ${event_id}


event-ticket-003 - Validar impossibilidade de cadastro de novo tipo de ingresso com capacidade superior
    [Tags]          TICKET_CRUD    FALHA    CAPACIDADE_EXCEDIDA
    [Documentation]    Tenta cadastrar 60 ingressos - Limite: 50/100 - Excede: 10
    
    ${event_id}=    Set Variable    ${GLOBAL_EVENT_ID}
    
    # 1. Gera payload com capacidade excedente
    ${ticket_name}=     FakerLibrary.Word
    &{payload}=     Criar Payload Ticket Basico    Social    ${120}     ${60}

    # 2. Executa POST do ticket
    ${response}=    Executar Post Ticket Para Evento    ${event_id}     ${payload}      ${409}
    


event-ticket-004 - Validar impossibilidade de cadastro com data de início superior à do evento
    [Tags]          TICKET_CRUD    FALHA    DATA_INVALIDA
    [Documentation]    Tenta cadastrar com data inicial após término do evento
    
    ${event_id}=    Set Variable    ${GLOBAL_EVENT_ID}
    ${data_venda_invalida}=     Set Variable    2030-10-10T22:00:00
    
    # 1. Gera payload com data inválida
    ${ticket_name}=     Gerar Nome Ticket Aleatorio
    &{payload}=     Criar Payload Ticket Com Datas    ${ticket_name}   ${130}    ${20}    ${data_venda_invalida}    ${FUTURE_DATE_FINAL}
    
    # 2. Executa POST do ticket
    ${response}=    Executar Post Ticket Para Evento    ${event_id}     ${payload}      ${409}
    


event-ticket-005 - Validar impossibilidade de cadastro com nome de tipo de ingresso duplicado
    [Tags]          TICKET_CRUD    FALHA    NOME_DUPLICADO
    [Documentation]    Tenta cadastrar com nome já existente (do cenário 001)
    
    ${event_id}=    Set Variable    ${GLOBAL_EVENT_ID}

    # 1. Gera payload com nome duplicado
    &{payload}=     Criar Payload Ticket Basico    ${GLOBAL_TICKET_NAME_1}    ${130}    ${10}
    
    # 2. Executa POST do ticket
    ${response}=    Executar Post Ticket Para Evento    ${event_id}     ${payload}      ${409}
    



event-ticket-006 - Validar impossibilidade de cadastro para evento inexistente
    [Tags]          TICKET_CRUD    FALHA    EVENTO_INEXISTENTE
    [Documentation]    Tenta cadastrar para UUID que não existe
    
    # 1. ID inexistente e payload
    ${uuid_inexistente}=    Set Variable    12345678-0000-0000-0000-000000000000
    ${ticket_name}=     Gerar Nome Ticket Aleatorio
    &{payload}=     Criar Payload Ticket Basico    ${ticket_name}   ${120}    ${10}
    
    # 2. Executa POST do ticket
    ${response}=    Executar Post Ticket Para Evento    ${uuid_inexistente}     ${payload}      ${404}


event-ticket-007 - Validar cadastro com capacidade complementar e data de início no dia do evento
    [Tags]          TICKET_CRUD    SUCESSO    DATA_EVENTO
    [Documentation]    Cadastra último lote (50) com data inicial igual à do evento
    
    ${event_id}=    Set Variable    ${GLOBAL_EVENT_ID}
    
    # 1. Capacidade: 50 restantes (100 - 50 alocados)
    ${ticket_name}=     Gerar Nome Ticket Aleatorio
    &{payload}=     Criar Payload Ticket Com Datas    ${ticket_name}  ${240}   ${50}    ${FUTURE_DATE_INITIAL}    ${FUTURE_DATE_FINAL}
    
    # 2. Executa POST do ticket
    ${response}=    Executar Post Ticket Para Evento    ${event_id}     ${payload}      ${201}
    
    # 3. Validações
    Validar Response Ticket Sucesso 201      ${response}     ${ticket_name}    ${event_id}